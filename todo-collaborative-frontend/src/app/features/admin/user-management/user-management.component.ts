import { DatePipe } from '@angular/common';
import { Component, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { UtilisateurResponse } from '../../../core/models/utilisateur.model';
import { UtilisateurService } from '../../../core/services/utilisateur.service';
import { UserEditFormComponent } from './user-edit-form.component';

const PAGE_SIZE = 20;

@Component({
  selector: 'app-user-management',
  standalone: true,
  imports: [FormsModule, DatePipe, UserEditFormComponent],
  template: `
    <h1 class="text-xl font-semibold mb-6">Gestion des utilisateurs</h1>

    <div class="flex gap-3 mb-4">
      <input
        [(ngModel)]="pseudoFilter"
        (ngModelChange)="onFilterChange()"
        placeholder="Filtrer par pseudo..."
        class="border border-gray-300 rounded-md px-3 py-2 text-sm flex-1 focus:outline-none focus:ring-2 focus:ring-brand-500"
      />
      <label class="flex items-center gap-2 text-sm text-gray-600">
        <input type="checkbox" [(ngModel)]="actifsOnly" (ngModelChange)="onFilterChange()" />
        Actifs uniquement
      </label>
    </div>

    @if (editingUser(); as editing) {
      <app-user-edit-form
        [user]="editing"
        (updated)="onUserUpdated($event)"
        (cancelled)="editingUser.set(null)"
      ></app-user-edit-form>
    }

    @if (loading()) {
      <p class="text-sm text-gray-400">Chargement...</p>
    } @else {
      <div class="bg-white border border-gray-200 rounded-lg overflow-hidden">
        <table class="w-full text-sm">
          <thead class="bg-gray-50 text-gray-500 text-left">
            <tr>
              <th class="px-4 py-2 font-medium">Pseudo</th>
              <th class="px-4 py-2 font-medium">Email</th>
              <th class="px-4 py-2 font-medium">Rôles</th>
              <th class="px-4 py-2 font-medium">Statut</th>
              <th class="px-4 py-2 font-medium">Inscrit le</th>
              <th class="px-4 py-2"></th>
            </tr>
          </thead>
          <tbody>
            @for (u of users(); track u.id) {
              <tr class="border-t border-gray-100">
                <td class="px-4 py-2">{{ u.pseudo }}</td>
                <td class="px-4 py-2 text-gray-500">{{ u.email }}</td>
                <td class="px-4 py-2 text-gray-500">{{ u.roles.join(', ') }}</td>
                <td class="px-4 py-2">
                  <span
                    class="text-xs px-2 py-0.5 rounded-full"
                    [class.bg-green-100]="u.actif"
                    [class.text-green-700]="u.actif"
                    [class.bg-gray-100]="!u.actif"
                    [class.text-gray-500]="!u.actif"
                  >
                    {{ u.actif ? 'Actif' : 'Inactif' }}
                  </span>
                </td>
                <td class="px-4 py-2 text-gray-400">
                  {{ u.dateCreation | date: 'dd/MM/yyyy' }}
                </td>
                <td class="px-4 py-2 text-right whitespace-nowrap">
                  <button
                    (click)="editingUser.set(u)"
                    class="text-xs text-brand-700 hover:text-brand-800 mr-3"
                  >
                    Modifier
                  </button>
                  <button
                    (click)="deleteUser(u)"
                    class="text-xs text-red-500 hover:text-red-700"
                  >
                    Supprimer
                  </button>
                </td>
              </tr>
            }
          </tbody>
        </table>
      </div>

      @if (users().length === 0) {
        <p class="text-sm text-gray-400 mt-3">Aucun utilisateur ne correspond au filtre.</p>
      }

      @if (totalPages() > 1) {
        <div class="flex items-center justify-between mt-4 text-sm">
          <span class="text-gray-400">
            Page {{ currentPage() + 1 }} / {{ totalPages() }} · {{ totalElements() }} utilisateur(s)
          </span>
          <div class="flex gap-2">
            <button
              (click)="goToPage(currentPage() - 1)"
              [disabled]="currentPage() === 0"
              class="px-3 py-1.5 rounded-md bg-gray-100 hover:bg-gray-200 disabled:opacity-40"
            >
              Précédent
            </button>
            <button
              (click)="goToPage(currentPage() + 1)"
              [disabled]="currentPage() + 1 >= totalPages()"
              class="px-3 py-1.5 rounded-md bg-gray-100 hover:bg-gray-200 disabled:opacity-40"
            >
              Suivant
            </button>
          </div>
        </div>
      }
    }
  `,
})
export class UserManagementComponent {
  private utilisateurService = inject(UtilisateurService);

  users = signal<UtilisateurResponse[]>([]);
  loading = signal(false);
  pseudoFilter = '';
  actifsOnly = false;

  currentPage = signal(0);
  totalPages = signal(0);
  totalElements = signal(0);

  editingUser = signal<UtilisateurResponse | null>(null);

  private filterTimeout?: ReturnType<typeof setTimeout>;

  constructor() {
    this.fetch();
  }

  onFilterChange(): void {
    // petit debounce pour éviter une requête à chaque frappe
    clearTimeout(this.filterTimeout);
    this.filterTimeout = setTimeout(() => {
      this.currentPage.set(0); // on repart de la page 1 à chaque changement de filtre
      this.fetch();
    }, 300);
  }

  goToPage(page: number): void {
    if (page < 0 || page >= this.totalPages()) return;
    this.currentPage.set(page);
    this.fetch();
  }

  onUserUpdated(updated: UtilisateurResponse): void {
    this.users.update((list) => list.map((u) => (u.id === updated.id ? updated : u)));
    this.editingUser.set(null);
  }

  deleteUser(user: UtilisateurResponse): void {
    if (!confirm(`Supprimer le compte de ${user.pseudo} ?`)) return;
    this.utilisateurService.delete(user.id).subscribe({
      next: () => {
        this.users.update((list) => list.filter((u) => u.id !== user.id));
        this.totalElements.update((n) => n - 1);
      },
    });
  }

  private fetch(): void {
    this.loading.set(true);
    this.utilisateurService
      .getAll(
        this.currentPage(),
        PAGE_SIZE,
        this.pseudoFilter || undefined,
        this.actifsOnly || undefined
      )
      .subscribe({
        next: (page) => {
          this.users.set(page.content);
          this.totalPages.set(page.totalPages);
          this.totalElements.set(page.totalElements);
          this.loading.set(false);
        },
        error: () => this.loading.set(false),
      });
  }
}
