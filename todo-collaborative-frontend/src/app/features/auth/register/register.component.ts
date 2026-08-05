import { HttpErrorResponse } from '@angular/common/http';
import { Component, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { ErrorResponse } from '../../../core/models/error.model';
import { AuthService } from '../../../core/services/auth.service';

// Miroir des contraintes Bean Validation de RegisterRequest.java
const PSEUDO_PATTERN = /^[a-zA-Z0-9_]+$/;
const PASSWORD_PATTERN = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&]).+$/;

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [ReactiveFormsModule, RouterLink],
  template: `
    <div class="max-w-sm mx-auto mt-12 bg-white p-6 rounded-lg border border-gray-200">
      <h1 class="text-xl font-semibold mb-6">Créer un compte</h1>

      <form [formGroup]="form" (ngSubmit)="submit()" class="space-y-4">
        <div>
          <label class="block text-sm text-gray-600 mb-1">Pseudo</label>
          <input
            formControlName="pseudo"
            class="w-full border border-gray-300 rounded-md px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-brand-500"
          />
          @if (form.controls.pseudo.touched && form.controls.pseudo.invalid) {
            <p class="text-xs text-red-600 mt-1">
              3 à 30 caractères : lettres, chiffres, underscore uniquement.
            </p>
          }
        </div>

        <div>
          <label class="block text-sm text-gray-600 mb-1">Email</label>
          <input
            type="email"
            formControlName="email"
            class="w-full border border-gray-300 rounded-md px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-brand-500"
          />
        </div>

        <div>
          <label class="block text-sm text-gray-600 mb-1">Mot de passe</label>
          <input
            type="password"
            formControlName="password"
            class="w-full border border-gray-300 rounded-md px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-brand-500"
          />
          @if (form.controls.password.touched && form.controls.password.invalid) {
            <p class="text-xs text-red-600 mt-1">
              8 caractères min., avec majuscule, minuscule, chiffre et caractère spécial
              (&#64;$!%*?&amp;).
            </p>
          }
        </div>

        @if (errorMessage()) {
          <p class="text-sm text-red-600">{{ errorMessage() }}</p>
        }

        <button
          type="submit"
          [disabled]="form.invalid || loading()"
          class="w-full bg-brand-600 text-white rounded-md py-2 text-sm font-medium hover:bg-brand-700 disabled:opacity-50"
        >
          {{ loading() ? 'Création...' : 'Créer mon compte' }}
        </button>
      </form>

      <p class="text-sm text-gray-500 mt-4 text-center">
        Déjà un compte ?
        <a routerLink="/login" class="text-brand-700 font-medium">Se connecter</a>
      </p>
    </div>
  `,
})
export class RegisterComponent {
  private fb = inject(FormBuilder);
  private authService = inject(AuthService);
  private router = inject(Router);

  loading = signal(false);
  errorMessage = signal<string | null>(null);

  form = this.fb.nonNullable.group({
    pseudo: [
      '',
      [Validators.required, Validators.minLength(3), Validators.maxLength(30), Validators.pattern(PSEUDO_PATTERN)],
    ],
    email: ['', [Validators.required, Validators.email]],
    password: ['', [Validators.required, Validators.minLength(8), Validators.pattern(PASSWORD_PATTERN)]],
  });

  submit(): void {
    if (this.form.invalid) return;
    this.loading.set(true);
    this.errorMessage.set(null);

    this.authService.register(this.form.getRawValue()).subscribe({
      next: () => {
        this.loading.set(false);
        this.router.navigateByUrl('/lists');
      },
      error: (err: HttpErrorResponse) => {
        this.loading.set(false);
        const body = err.error as ErrorResponse | undefined;
        const detailMsg = body?.details ? Object.values(body.details).join(' ') : undefined;
        this.errorMessage.set(detailMsg ?? body?.message ?? "Impossible de créer le compte.");
      },
    });
  }
}
