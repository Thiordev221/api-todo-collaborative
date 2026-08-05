import { HttpClient } from '@angular/common/http';
import { Injectable, computed, inject, signal } from '@angular/core';
import { Router } from '@angular/router';
import { catchError, map, Observable, of, tap } from 'rxjs';
import { environment } from '../../../environments/environment.development';
import {
  AuthResponse,
  LoginRequest,
  RegisterRequest,
} from '../models/auth.model';

interface Session {
  accessToken: string;
  userId: number;
  email: string;
  pseudo: string;
  roles: string[];
}

/**
 * Le refresh token vit UNIQUEMENT dans un cookie httpOnly posé par le backend
 * (voir AuthController#withRefreshCookie côté Spring Boot). Ce service ne le
 * voit jamais et ne le stocke jamais : impossible pour un script XSS de le lire.
 *
 * L'access token, lui, ne vit qu'en mémoire (signal), jamais sur disque
 * (ni localStorage, ni sessionStorage). Il est donc perdu à chaque rechargement
 * de page — c'est voulu. Pour restaurer la session au démarrage, on tente un
 * refresh silencieux (voir `tryRestoreSession`, appelé une seule fois au
 * bootstrap de l'app dans app.config.ts).
 */
@Injectable({ providedIn: 'root' })
export class AuthService {
  private http = inject(HttpClient);
  private router = inject(Router);
  private baseUrl = `${environment.apiUrl}/auth`;

  private sessionSignal = signal<Session | null>(null);

  readonly session = this.sessionSignal.asReadonly();
  readonly isAuthenticated = computed(() => this.sessionSignal() !== null);
  readonly isAdmin = computed(() =>
    (this.sessionSignal()?.roles ?? []).includes('ROLE_ADMIN')
  );
  readonly currentUserId = computed(() => this.sessionSignal()?.userId ?? null);
  readonly currentPseudo = computed(() => this.sessionSignal()?.pseudo ?? null);

  register(request: RegisterRequest): Observable<AuthResponse> {
    return this.http
      .post<AuthResponse>(`${this.baseUrl}/register`, request, { withCredentials: true })
      .pipe(tap((res) => this.persistSession(res)));
  }

  login(request: LoginRequest): Observable<AuthResponse> {
    return this.http
      .post<AuthResponse>(`${this.baseUrl}/login`, request, { withCredentials: true })
      .pipe(tap((res) => this.persistSession(res)));
  }

  /** Le cookie refreshToken part automatiquement (withCredentials) — pas de body à envoyer. */
  refresh(): Observable<AuthResponse> {
    return this.http
      .post<AuthResponse>(`${this.baseUrl}/refresh`, {}, { withCredentials: true })
      .pipe(tap((res) => this.persistSession(res)));
  }

  logout(): void {
    this.http.post(`${this.baseUrl}/logout`, {}, { withCredentials: true }).subscribe({
      complete: () => this.clearSession(),
      error: () => this.clearSession(),
    });
  }

  /**
   * Appelé une fois au démarrage de l'app (APP_INITIALIZER). Tente de restaurer
   * la session via le cookie httpOnly. Ne bloque jamais le démarrage en cas
   * d'échec (utilisateur simplement non connecté).
   */
  tryRestoreSession(): Observable<boolean> {
    return this.refresh().pipe(
      map(() => true),
      catchError(() => of(false))
    );
  }

  getAccessToken(): string | null {
    return this.sessionSignal()?.accessToken ?? null;
  }

  getUserId(): number | null {
    return this.sessionSignal()?.userId ?? null;
  }

  private persistSession(res: AuthResponse): void {
    this.sessionSignal.set({
      accessToken: res.accessToken,
      userId: res.userId,
      email: res.email,
      pseudo: res.pseudo,
      roles: res.roles,
    });
  }

  private clearSession(): void {
    this.sessionSignal.set(null);
    this.router.navigateByUrl('/login');
  }
}

