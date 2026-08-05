import { HttpErrorResponse } from '@angular/common/http';
import { Component, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service';
import { ErrorResponse } from '../../../core/models/error.model';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [ReactiveFormsModule, RouterLink],
  template: `
    <div class="max-w-sm mx-auto mt-12 bg-white p-6 rounded-lg border border-gray-200">
      <h1 class="text-xl font-semibold mb-6">Connexion</h1>

      <form [formGroup]="form" (ngSubmit)="submit()" class="space-y-4">
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
        </div>

        @if (errorMessage()) {
          <p class="text-sm text-red-600">{{ errorMessage() }}</p>
        }

        <button
          type="submit"
          [disabled]="form.invalid || loading()"
          class="w-full bg-brand-600 text-white rounded-md py-2 text-sm font-medium hover:bg-brand-700 disabled:opacity-50"
        >
          {{ loading() ? 'Connexion...' : 'Se connecter' }}
        </button>
      </form>

      <p class="text-sm text-gray-500 mt-4 text-center">
        Pas encore de compte ?
        <a routerLink="/register" class="text-brand-700 font-medium">Créer un compte</a>
      </p>
    </div>
  `,
})
export class LoginComponent {
  private fb = inject(FormBuilder);
  private authService = inject(AuthService);
  private router = inject(Router);

  loading = signal(false);
  errorMessage = signal<string | null>(null);

  form = this.fb.nonNullable.group({
    email: ['', [Validators.required, Validators.email]],
    password: ['', [Validators.required, Validators.minLength(6)]],
  });

  submit(): void {
    if (this.form.invalid) return;
    this.loading.set(true);
    this.errorMessage.set(null);

    this.authService.login(this.form.getRawValue()).subscribe({
      next: () => {
        this.loading.set(false);
        this.router.navigateByUrl('/lists');
      },
      error: (err: HttpErrorResponse) => {
        this.loading.set(false);
        const body = err.error as ErrorResponse | undefined;
        this.errorMessage.set(body?.message ?? 'Email ou mot de passe incorrect.');
      },
    });
  }
}
