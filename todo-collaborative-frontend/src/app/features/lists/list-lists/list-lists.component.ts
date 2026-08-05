import { Component, inject, signal } from '@angular/core';
import { RouterLink } from '@angular/router';
import { TodoListResponse } from '../../../core/models/todo-list.model';
import { TodoListService } from '../../../core/services/todo-list.service';
import { ListFormComponent } from '../list-form/list-form.component';

type Tab = 'mine' | 'sharedByMe' | 'sharedWithMe';

@Component({
  selector: 'app-list-lists',
  standalone: true,
  imports: [RouterLink, ListFormComponent],
  template: `
    <div class="flex items-center justify-between mb-6">
      <h1 class="text-xl font-semibold">Mes Todo Listes</h1>
      <button
        (click)="showCreateForm.set(true)"
        class="px-3 py-1.5 rounded-md bg-brand-600 text-white text-sm hover:bg-brand-700"
      >
        + Nouvelle liste
      </button>
    </div>

    @if (showCreateForm()) {
      <app-list-form
        (created)="onListCreated()"
        (cancelled)="showCreateForm.set(false)"
      ></app-list-form>
    }

    <div class="flex gap-1 border-b border-gray-200 mb-4 text-sm">
      @for (t of tabs; track t.key) {
        <button
          (click)="switchTab(t.key)"
          class="px-3 py-2 border-b-2 -mb-px"
          [class.border-brand-600]="activeTab() === t.key"
          [class.text-brand-700]="activeTab() === t.key"
          [class.border-transparent]="activeTab() !== t.key"
          [class.text-gray-500]="activeTab() !== t.key"
        >
          {{ t.label }}
        </button>
      }
    </div>

    @if (loading()) {
      <p class="text-gray-400 text-sm">Chargement...</p>
    } @else if (lists().length === 0) {
      <p class="text-gray-400 text-sm">Aucune liste ici pour le moment.</p>
    } @else {
      <div class="grid gap-3">
        @for (list of lists(); track list.id) {
          <a
            [routerLink]="['/lists', list.id]"
            class="block bg-white border border-gray-200 rounded-lg p-4 hover:border-brand-400 transition"
          >
            <div class="flex items-center justify-between">
              <h2 class="font-medium text-gray-800">{{ list.titre }}</h2>
              <span class="text-xs px-2 py-0.5 rounded-full bg-gray-100 text-gray-600">
                {{ list.nbTaches }} tâche(s)
              </span>
            </div>
            @if (list.description) {
              <p class="text-sm text-gray-500 mt-1">{{ list.description }}</p>
            }
            <p class="text-xs text-gray-400 mt-2">
              Propriétaire : {{ list.proprietairePseudo }} · Permission :
              {{ list.mesPermissions }}
            </p>
          </a>
        }
      </div>
    }
  `,
})
export class ListListsComponent {
  private todoListService = inject(TodoListService);

  tabs: { key: Tab; label: string }[] = [
    { key: 'mine', label: 'Mes listes' },
    { key: 'sharedByMe', label: 'Partagées par moi' },
    { key: 'sharedWithMe', label: 'Partagées avec moi' },
  ];

  activeTab = signal<Tab>('mine');
  lists = signal<TodoListResponse[]>([]);
  loading = signal(false);
  showCreateForm = signal(false);

  constructor() {
    this.fetch();
  }

  switchTab(tab: Tab): void {
    if (this.activeTab() === tab) return;
    this.activeTab.set(tab);
    this.fetch();
  }

  onListCreated(): void {
    this.showCreateForm.set(false);
    this.activeTab.set('mine');
    this.fetch();
  }

  private fetch(): void {
    this.loading.set(true);
    const obs =
      this.activeTab() === 'mine'
        ? this.todoListService.getMyLists()
        : this.activeTab() === 'sharedByMe'
          ? this.todoListService.getSharedByMe()
          : this.todoListService.getSharedWithMe();

    obs.subscribe({
      next: (page) => {
        this.lists.set(page.content);
        this.loading.set(false);
      },
      error: () => this.loading.set(false),
    });
  }
}
