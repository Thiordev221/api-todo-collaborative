import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment.development';
import { Page } from '../models/page.model';
import { TodoListRequest, TodoListResponse } from '../models/todo-list.model';

@Injectable({ providedIn: 'root' })
export class TodoListService {
  private http = inject(HttpClient);
  private baseUrl = `${environment.apiUrl}/lists`;

  create(request: TodoListRequest): Observable<TodoListResponse> {
    return this.http.post<TodoListResponse>(this.baseUrl, request);
  }

  getById(id: number): Observable<TodoListResponse> {
    return this.http.get<TodoListResponse>(`${this.baseUrl}/${id}`);
  }

  getMyLists(page = 0, size = 20): Observable<Page<TodoListResponse>> {
    const params = new HttpParams().set('page', page).set('size', size);
    return this.http.get<Page<TodoListResponse>>(`${this.baseUrl}/my-lists`, { params });
  }

  getSharedByMe(page = 0, size = 20): Observable<Page<TodoListResponse>> {
    const params = new HttpParams().set('page', page).set('size', size);
    return this.http.get<Page<TodoListResponse>>(`${this.baseUrl}/sharedByUser`, { params });
  }

  getSharedWithMe(page = 0, size = 20): Observable<Page<TodoListResponse>> {
    const params = new HttpParams().set('page', page).set('size', size);
    return this.http.get<Page<TodoListResponse>>(`${this.baseUrl}/sharedToUser`, { params });
  }

  update(listId: number, request: TodoListRequest): Observable<TodoListResponse> {
    return this.http.put<TodoListResponse>(`${this.baseUrl}/${listId}`, request);
  }

  delete(listId: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${listId}`);
  }
}
