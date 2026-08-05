import { DatePipe } from '@angular/common';
import { Component, computed, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { Permission, PartageResponse } from '../../../core/models/partage.model';
import { TacheResponse } from '../../../core/models/tache.model';
import { TodoListResponse } from '../../../core/models/todo-list.model';
import { PartageService } from '../../../core/services/partage.service';
import { TacheService } from '../../../core/services/tache.service';
import { TodoListService } from '../../../core/services/todo-list.service';
import { ListFormComponent } from '../list-form/list-form.component';

@Component({
  selector: 'app-list-detail',
  standalone: true,
  imports: [ReactiveFormsModule, RouterLink, ListFormComponent, DatePipe],
  template: `
    @if (list()) {
      <div class="mb-4">
        <a routerLink="/lists" class="text-sm text-brand-700">&larr; Retour aux listes</a>
      </div>

      @if (editingList()) {
        <app-list-form
          [existingList]="list()"
          (created)="onListUpdated($event)"
          (cancelled)="editingList.set(false)"
        ></app-list-form>
      } @else {
        <div class="bg-white border border-gray-200 rounded-lg p-4 mb-6">
          <div class="flex items-start justify-between">
            <div>
              <h1 class="text-xl font-semibold">{{ list()!.titre }}</h1>
              @if (list()!.description) {
                <p class="text-sm text-gray-500 mt-1">{{ list()!.description }}</p>
              }
              <p class="text-xs text-gray-400 mt-2">
                Propriétaire : {{ list()!.proprietairePseudo }} · Ma permission :
                {{ list()!.mesPermissions }}
              </p>
            </div>
            @if (isOwner()) {
              <div class="flex gap-2">
                <button
                  (click)="editingList.set(true)"
                  class="text-sm px-3 py-1.5 rounded-md bg-gray-100 hover:bg-gray-200"
                >
                  Modifier
                </button>
                <button
                  (click)="deleteList()"
                  class="text-sm px-3 py-1.5 rounded-md bg-red-50 text-red-600 hover:bg-red-100"
                >
                  Supprimer
                </button>
              </div>
            }
          </div>
        </div>
      }

      <!-- ===== TÂCHES ===== -->
      <section class="mb-8">
        <div class="flex items-center justify-between mb-3">
          <h2 class="font-medium text-gray-800">Tâches</h2>
          @if (canEdit()) {
            <button
              (click)="showTacheForm.set(!showTacheForm())"
              class="text-sm px-3 py-1.5 rounded-md bg-brand-600 text-white hover:bg-brand-700"
            >
              + Ajouter une tâche
            </button>
          }
        </div>

        @if (showTacheForm()) {
          <form
            [formGroup]="tacheForm"
            (ngSubmit)="submitTache()"
            class="bg-white border border-gray-200 rounded-lg p-4 mb-3 space-y-3"
          >
            <input
              formControlName="titre"
              placeholder="Titre de la tâche"
              maxlength="150"
              class="w-full border border-gray-300 rounded-md px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-brand-500"
            />
            <textarea
              formControlName="description"
              placeholder="Description (optionnelle)"
              maxlength="1000"
              rows="2"
              class="w-full border border-gray-300 rounded-md px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-brand-500"
            ></textarea>
            <input
              type="datetime-local"
              formControlName="echeance"
              class="border border-gray-300 rounded-md px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-brand-500"
            />
            <div class="flex gap-2">
              <button
                type="submit"
                [disabled]="tacheForm.invalid"
                class="px-3 py-1.5 rounded-md bg-brand-600 text-white text-sm hover:bg-brand-700 disabled:opacity-50"
              >
                Ajouter
              </button>
              <button
                type="button"
                (click)="showTacheForm.set(false)"
                class="px-3 py-1.5 rounded-md bg-gray-100 text-gray-700 text-sm hover:bg-gray-200"
              >
                Annuler
              </button>
            </div>
          </form>
        }

        @if (taches().length === 0) {
          <p class="text-sm text-gray-400">Aucune tâche pour l'instant.</p>
        } @else {
          <ul class="space-y-2">
            @for (tache of taches(); track tache.id) {
              <li
                class="bg-white border border-gray-200 rounded-lg p-3 flex items-start gap-3"
              >
                <input
                  type="checkbox"
                  [checked]="tache.termine"
                  [disabled]="!canEdit()"
                  (change)="toggleTache(tache)"
                  class="mt-1"
                />
                <div class="flex-1">
                  <p
                    class="text-sm font-medium"
                    [class.line-through]="tache.termine"
                    [class.text-gray-400]="tache.termine"
                  >
                    {{ tache.titre }}
                  </p>
                  @if (tache.description) {
                    <p class="text-xs text-gray-500">{{ tache.description }}</p>
                  }
                  @if (tache.echeance) {
                    <p class="text-xs text-gray-400 mt-1">
                      Échéance : {{ tache.echeance | date: 'dd/MM/yyyy HH:mm' }}
                    </p>
                  }
                </div>
                @if (canEdit()) {
                  <button
                    (click)="deleteTache(tache)"
                    class="text-xs text-red-500 hover:text-red-700"
                  >
                    Supprimer
                  </button>
                }
              </li>
            }
          </ul>
        }
      </section>

      <!-- ===== PARTAGES (propriétaire uniquement) ===== -->
      @if (isOwner()) {
        <section>
          <h2 class="font-medium text-gray-800 mb-3">Partages</h2>

          <form
            [formGroup]="partageForm"
            (ngSubmit)="submitPartage()"
            class="bg-white border border-gray-200 rounded-lg p-4 mb-3 flex gap-2 items-end flex-wrap"
          >
            <div class="flex-1 min-w-[180px]">
              <label class="block text-xs text-gray-600 mb-1">Email de l'invité</label>
              <input
                type="email"
                formControlName="inviteEmail"
                class="w-full border border-gray-300 rounded-md px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-brand-500"
              />
            </div>
            <div>
              <label class="block text-xs text-gray-600 mb-1">Permission</label>
              <select
                formControlName="permission"
                class="border border-gray-300 rounded-md px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-brand-500"
              >
                <option value="ONLY_READ">Lecture seule</option>
                <option value="READ_WRITE">Lecture / écriture</option>
              </select>
            </div>
            <button
              type="submit"
              [disabled]="partageForm.invalid"
              class="px-3 py-2 rounded-md bg-brand-600 text-white text-sm hover:bg-brand-700 disabled:opacity-50"
            >
              Inviter
            </button>
          </form>

          @if (partageErrorMessage()) {
            <p class="text-sm text-red-600 mb-2">{{ partageErrorMessage() }}</p>
          }

          @if (partages().length === 0) {
            <p class="text-sm text-gray-400">Personne n'a encore accès à cette liste.</p>
          } @else {
            <ul class="space-y-2">
              @for (partage of partages(); track partage.id) {
                <li
                  class="bg-white border border-gray-200 rounded-lg p-3 flex items-center justify-between"
                >
                  <div>
                    <p class="text-sm font-medium">{{ partage.invitePseudo }}</p>
                    <p class="text-xs text-gray-400">
                      {{ partage.inviteEmail }} ·
                      {{ partage.permission === 'ONLY_READ' ? 'Lecture seule' : 'Lecture / écriture' }}
                    </p>
                  </div>
                  <button
                    (click)="revoquerPartage(partage)"
                    class="text-xs text-red-500 hover:text-red-700"
                  >
                    Révoquer
                  </button>
                </li>
              }
            </ul>
          }
        </section>
      }
    } @else {
      <p class="text-sm text-gray-400">Chargement de la liste...</p>
    }
  `,
})
export class ListDetailComponent {
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private fb = inject(FormBuilder);
  private todoListService = inject(TodoListService);
  private tacheService = inject(TacheService);
  private partageService = inject(PartageService);

  listId = Number(this.route.snapshot.paramMap.get('id'));

  list = signal<TodoListResponse | null>(null);
  taches = signal<TacheResponse[]>([]);
  partages = signal<PartageResponse[]>([]);

  editingList = signal(false);
  showTacheForm = signal(false);
  partageErrorMessage = signal<string | null>(null);

  isOwner = computed(() => this.list()?.mesPermissions === 'OWNER');
  canEdit = computed(() => {
    const perm = this.list()?.mesPermissions;
    return perm === 'OWNER' || perm === 'EDITOR';
  });

  tacheForm = this.fb.nonNullable.group({
    titre: ['', [Validators.required, Validators.maxLength(150)]],
    description: ['', [Validators.maxLength(1000)]],
    echeance: [''],
  });

  partageForm = this.fb.nonNullable.group({
    inviteEmail: ['', [Validators.required, Validators.email]],
    permission: ['ONLY_READ' as Permission, [Validators.required]],
  });

  constructor() {
    this.fetchList();
    this.fetchTaches();
  }

  private fetchList(): void {
    this.todoListService.getById(this.listId).subscribe({
      next: (res) => {
        this.list.set(res);
        if (res.mesPermissions === 'OWNER') {
          this.fetchPartages();
        }
      },
      error: () => this.router.navigateByUrl('/lists'),
    });
  }

  private fetchTaches(): void {
    this.tacheService.getByListe(this.listId).subscribe({
      next: (page) => this.taches.set(page.content),
    });
  }

  private fetchPartages(): void {
    this.partageService.getByListe(this.listId).subscribe({
      next: (page) => this.partages.set(page.content),
    });
  }

  onListUpdated(updated: TodoListResponse): void {
    this.list.set(updated);
    this.editingList.set(false);
  }

  deleteList(): void {
    if (!confirm('Supprimer définitivement cette liste et ses tâches ?')) return;
    this.todoListService.delete(this.listId).subscribe({
      next: () => this.router.navigateByUrl('/lists'),
    });
  }

  submitTache(): void {
    if (this.tacheForm.invalid) return;
    const raw = this.tacheForm.getRawValue();
    this.tacheService
      .create(this.listId, {
        titre: raw.titre,
        description: raw.description || undefined,
        echeance: raw.echeance || null,
      })
      .subscribe({
        next: (tache) => {
          this.taches.update((list) => [...list, tache]);
          this.tacheForm.reset();
          this.showTacheForm.set(false);
        },
      });
  }

  toggleTache(tache: TacheResponse): void {
    this.tacheService.toggleStatus(this.listId, tache.id).subscribe({
      next: (updated) => {
        this.taches.update((list) =>
          list.map((t) => (t.id === updated.id ? updated : t))
        );
      },
    });
  }

  deleteTache(tache: TacheResponse): void {
    if (!confirm('Supprimer cette tâche ?')) return;
    this.tacheService.delete(this.listId, tache.id).subscribe({
      next: () => {
        this.taches.update((list) => list.filter((t) => t.id !== tache.id));
      },
    });
  }

  submitPartage(): void {
    if (this.partageForm.invalid) return;
    this.partageErrorMessage.set(null);
    const raw = this.partageForm.getRawValue();
    this.partageService.inviter(this.listId, raw).subscribe({
      next: (partage) => {
        this.partages.update((list) => [...list, partage]);
        this.partageForm.reset({ inviteEmail: '', permission: 'ONLY_READ' });
      },
      error: () => {
        this.partageErrorMessage.set(
          "Impossible d'inviter cet utilisateur (déjà invité, email inexistant, ou auto-partage)."
        );
      },
    });
  }

  revoquerPartage(partage: PartageResponse): void {
    if (!confirm(`Révoquer l'accès de ${partage.inviteEmail} ?`)) return;
    this.partageService.revoquer(this.listId, partage.inviteEmail).subscribe({
      next: () => {
        this.partages.update((list) => list.filter((p) => p.id !== partage.id));
      },
    });
  }
}
