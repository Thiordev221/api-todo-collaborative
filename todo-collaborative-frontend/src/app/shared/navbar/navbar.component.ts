import { Component, inject } from '@angular/core';
import { RouterLink, RouterLinkActive } from '@angular/router';
import { AuthService } from '../../core/services/auth.service';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [RouterLink, RouterLinkActive],
  template: `
    <nav class="bg-white border-b border-gray-200">
      <div class="max-w-5xl mx-auto px-4 h-14 flex items-center justify-between">
        <a routerLink="/lists" class="font-semibold text-brand-700">Todo Collaborative</a>

        @if (auth.isAuthenticated()) {
          <div class="flex items-center gap-4 text-sm">
            <a
              routerLink="/lists"
              routerLinkActive="text-brand-700 font-medium"
              class="text-gray-600 hover:text-brand-700"
              >Mes listes</a
            >
            @if (auth.isAdmin()) {
              <a
                routerLink="/admin/utilisateurs"
                routerLinkActive="text-brand-700 font-medium"
                class="text-gray-600 hover:text-brand-700"
                >Utilisateurs</a
              >
            }
            <span class="text-gray-400">{{ auth.currentPseudo() }}</span>
            <button
              (click)="auth.logout()"
              class="px-3 py-1.5 rounded-md bg-gray-100 hover:bg-gray-200 text-gray-700"
            >
              Déconnexion
            </button>
          </div>
        } @else {
          <div class="flex items-center gap-3 text-sm">
            <a routerLink="/login" class="text-gray-600 hover:text-brand-700">Connexion</a>
            <a
              routerLink="/register"
              class="px-3 py-1.5 rounded-md bg-brand-600 text-white hover:bg-brand-700"
              >Créer un compte</a
            >
          </div>
        }
      </div>
    </nav>
  `,
})
export class NavbarComponent {
  auth = inject(AuthService);
}
