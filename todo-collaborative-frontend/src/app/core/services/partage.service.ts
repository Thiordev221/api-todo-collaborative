import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment.development';
import { Page } from '../models/page.model';
import { PartageRequest, PartageResponse } from '../models/partage.model';

@Injectable({ providedIn: 'root' })
export class PartageService {
  private http = inject(HttpClient);
  private baseUrl = `${environment.apiUrl}/lists`;

  inviter(listId: number, request: PartageRequest): Observable<PartageResponse> {
    return this.http.post<PartageResponse>(`${this.baseUrl}/${listId}/partages`, request);
  }

  revoquer(listId: number, inviteEmail: string): Observable<void> {
    const params = new HttpParams().set('inviteEmail', inviteEmail);
    return this.http.delete<void>(`${this.baseUrl}/${listId}/partages`, { params });
  }

  getByListe(listId: number, page = 0, size = 20): Observable<Page<PartageResponse>> {
    const params = new HttpParams().set('page', page).set('size', size);
    return this.http.get<Page<PartageResponse>>(`${this.baseUrl}/${listId}/partages`, {
      params,
    });
  }
}
