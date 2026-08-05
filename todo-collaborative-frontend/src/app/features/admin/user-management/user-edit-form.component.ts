import { HttpErrorResponse } from '@angular/common/http';
import { Component, EventEmitter, Input, OnInit, Output, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { ErrorResponse } from '../../../core/models/error.model';
import { UtilisateurResponse } from '../../../core/models/utilisateur.model';
import { UtilisateurService } from '../../../core/services/utilisateur.service';

@Component({
  selector: 'app-user-edit-form',
  standalone: true,
  imports: [ReactiveFormsModule],
  template: `
    <div class="bg-white border border-gray-200 rounded-lg p-4 mb-4">
      <h3 class="font-medium mb-1">Modifier {{ user.pseudo }}</h3>
      <p class="text-xs text-gray-400 mb-3">{{ user.email }}</p>

      <form [formGroup]="form" (ngSubmit)="submit()" class="space-y-3">
        <div>
          <label class="block text-sm text-gray-600 mb-1">Pseudo</label>
          <input
            formControlName="pseudo"
            class="w-full border border-gray-300 rounded-md px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-brand-500"
          />
          @if (form.controls.pseudo.touched && form.controls.pseudo.invalid) {
            <p class="text-xs text-red-600 mt-1">Entre 3 et 50 caractères.</p>
          }
        </div>

        <div>
          <label class="block text-sm text-gray-600 mb-1">
            Nouveau mot de passe <span class="text-gray-400">(laisser vide pour ne pas changer)</span>
          </label>
          <input
            type="password"
            formControlName="password"
            class="w-full border border-gray-300 rounded-md px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-brand-500"
          />
          @if (form.controls.password.touched && form.controls.password.invalid) {
            <p class="text-xs text-red-600 mt-1">Au moins 6 caractères si renseigné.</p>
          }
        </div>

        @if (errorMessage()) {
          <p class="text-sm text-red-600">{{ errorMessage() }}</p>
        }

        <div class="flex gap-2">
          <button
            type="submit"
            [disabled]="form.invalid || loading()"
            class="px-3 py-1.5 rounded-md bg-brand-600 text-white text-sm hover:bg-brand-700 disabled:opacity-50"
          >
            {{ loading() ? 'Enregistrement...' : 'Enregistrer' }}
          </button>
          <button
            type="button"
            (click)="cancelled.emit()"
            class="px-3 py-1.5 rounded-md bg-gray-100 text-gray-700 text-sm hover:bg-gray-200"
          >
            Annuler
          </button>
        </div>
      </form>
    </div>
  `,
})
export class UserEditFormComponent implements OnInit {
  @Input({ required: true }) user!: UtilisateurResponse;
  @Output() updated = new EventEmitter<UtilisateurResponse>();
  @Output() cancelled = new EventEmitter<void>();

  private fb = inject(FormBuilder);
  private utilisateurService = inject(UtilisateurService);

  loading = signal(false);
  errorMessage = signal<string | null>(null);

  // Miroir de UtilisateurUpdateRequest côté backend : pseudo 3-50, password min 6 si renseigné
  form = this.fb.nonNullable.group({
    pseudo: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(50)]],
    password: ['', [Validators.minLength(6)]],
  });

  ngOnInit(): void {
    this.form.patchValue({ pseudo: this.user.pseudo });
  }

  submit(): void {
    if (this.form.invalid) return;
    this.loading.set(true);
    this.errorMessage.set(null);

    const raw = this.form.getRawValue();
    this.utilisateurService
      .update(this.user.id, {
        pseudo: raw.pseudo,
        // on n'envoie le password que s'il a été renseigné, pour ne pas l'écraser
        password: raw.password ? raw.password : undefined,
      })
      .subscribe({
        next: (res) => {
          this.loading.set(false);
          this.updated.emit(res);
        },
        error: (err: HttpErrorResponse) => {
          this.loading.set(false);
          const body = err.error as ErrorResponse | undefined;
          this.errorMessage.set(body?.message ?? "Impossible de modifier cet utilisateur.");
        },
      });
  }
}
