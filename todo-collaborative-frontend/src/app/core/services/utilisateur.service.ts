import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment.development';
import { Page } from '../models/page.model';
import { UtilisateurResponse, UtilisateurUpdateRequest } from '../models/utilisateur.model';

@Injectable({ providedIn: 'root' })
export class UtilisateurService {
  private http = inject(HttpClient);
  private baseUrl = `${environment.apiUrl}/admin/utilisateurs`;

  getAll(
    page = 0,
    size = 20,
    pseudo?: string,
    actif?: boolean
  ): Observable<Page<UtilisateurResponse>> {
    let params = new HttpParams().set('page', page).set('size', size);
    if (pseudo) params = params.set('pseudo', pseudo);
    if (actif !== undefined) params = params.set('actif', actif);
    return this.http.get<Page<UtilisateurResponse>>(this.baseUrl, { params });
  }

  getById(id: number): Observable<UtilisateurResponse> {
    return this.http.get<UtilisateurResponse>(`${this.baseUrl}/${id}`);
  }

  update(id: number, request: UtilisateurUpdateRequest): Observable<UtilisateurResponse> {
    return this.http.put<UtilisateurResponse>(`${this.baseUrl}/${id}`, request);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }
}
