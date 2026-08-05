import { Component, EventEmitter, Input, OnInit, Output, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { TodoListResponse } from '../../../core/models/todo-list.model';
import { TodoListService } from '../../../core/services/todo-list.service';

@Component({
  selector: 'app-list-form',
  standalone: true,
  imports: [ReactiveFormsModule],
  template: `
    <div class="bg-white border border-gray-200 rounded-lg p-4 mb-4">
      <h3 class="font-medium mb-3">{{ existingList ? 'Modifier la liste' : 'Nouvelle liste' }}</h3>

      <form [formGroup]="form" (ngSubmit)="submit()" class="space-y-3">
        <div>
          <label class="block text-sm text-gray-600 mb-1">Titre</label>
          <input
            formControlName="titre"
            maxlength="100"
            class="w-full border border-gray-300 rounded-md px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-brand-500"
          />
        </div>
        <div>
          <label class="block text-sm text-gray-600 mb-1">Description (optionnelle)</label>
          <textarea
            formControlName="description"
            maxlength="500"
            rows="2"
            class="w-full border border-gray-300 rounded-md px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-brand-500"
          ></textarea>
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
export class ListFormComponent implements OnInit {
  @Input() existingList: TodoListResponse | null = null;
  @Output() created = new EventEmitter<TodoListResponse>();
  @Output() cancelled = new EventEmitter<void>();

  private fb = inject(FormBuilder);
  private todoListService = inject(TodoListService);

  loading = signal(false);
  errorMessage = signal<string | null>(null);

  form = this.fb.nonNullable.group({
    titre: ['', [Validators.required, Validators.maxLength(100)]],
    description: ['', [Validators.maxLength(500)]],
  });

  ngOnInit(): void {
    if (this.existingList) {
      this.form.patchValue({
        titre: this.existingList.titre,
        description: this.existingList.description ?? '',
      });
    }
  }

  submit(): void {
    if (this.form.invalid) return;
    this.loading.set(true);
    this.errorMessage.set(null);

    const request = this.form.getRawValue();
    const obs = this.existingList
      ? this.todoListService.update(this.existingList.id, request)
      : this.todoListService.create(request);

    obs.subscribe({
      next: (res) => {
        this.loading.set(false);
        this.created.emit(res);
      },
      error: () => {
        this.loading.set(false);
        this.errorMessage.set("Impossible d'enregistrer la liste.");
      },
    });
  }
}
