import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment.development';
import { Page } from '../models/page.model';
import {
  TacheCreateRequest,
  TacheResponse,
  TacheUpdateRequest,
} from '../models/tache.model';

@Injectable({ providedIn: 'root' })
export class TacheService {
  private http = inject(HttpClient);
  private baseUrl = `${environment.apiUrl}/lists`;

  create(listId: number, request: TacheCreateRequest): Observable<TacheResponse> {
    return this.http.post<TacheResponse>(`${this.baseUrl}/${listId}/taches`, request);
  }

  update(
    listId: number,
    tacheId: number,
    request: TacheUpdateRequest
  ): Observable<TacheResponse> {
    return this.http.put<TacheResponse>(
      `${this.baseUrl}/${listId}/taches/${tacheId}`,
      request
    );
  }

  delete(listId: number, tacheId: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${listId}/taches/${tacheId}`);
  }

  getByListe(listId: number, page = 0, size = 50): Observable<Page<TacheResponse>> {
    const params = new HttpParams().set('page', page).set('size', size);
    return this.http.get<Page<TacheResponse>>(`${this.baseUrl}/${listId}/taches`, { params });
  }

  getById(listId: number, tacheId: number): Observable<TacheResponse> {
    return this.http.get<TacheResponse>(`${this.baseUrl}/${listId}/taches/${tacheId}`);
  }

  toggleStatus(listId: number, tacheId: number): Observable<TacheResponse> {
    return this.http.patch<TacheResponse>(
      `${this.baseUrl}/${listId}/taches/${tacheId}/toggleStatus`,
      {}
    );
  }
}
