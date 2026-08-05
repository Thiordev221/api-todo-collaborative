import { HttpErrorResponse, HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { Router } from '@angular/router';
import { catchError, switchMap, throwError } from 'rxjs';
import { AuthService } from '../services/auth.service';

/**
 * - Ajoute Authorization: Bearer <accessToken> et X-User-Id sur les routes métier.
 * - withCredentials: true sur TOUTES les requêtes vers l'API, pour que le cookie
 *   httpOnly refreshToken parte automatiquement sur /auth/refresh et /auth/logout
 *   (le navigateur ne l'attache que si withCredentials est explicitement activé
 *   en cross-origin, ici localhost:4200 -> localhost:8080).
 * - Sur un 401, tente un refresh unique (cookie envoyé automatiquement, pas de
 *   body à fournir) puis rejoue la requête originale.
 */
export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const authService = inject(AuthService);
  const router = inject(Router);

  const isAuthEndpoint = req.url.includes('/auth/');
  const token = authService.getAccessToken();
  const userId = authService.getUserId();

  const headers: Record<string, string> = {};
  if (token && !isAuthEndpoint) {
    headers['Authorization'] = `Bearer ${token}`;
    if (userId !== null) headers['X-User-Id'] = String(userId);
  }

  const authReq = req.clone({ setHeaders: headers, withCredentials: true });

  return next(authReq).pipe(
    catchError((error: HttpErrorResponse) => {
      if (error.status === 401 && !isAuthEndpoint) {
        return authService.refresh().pipe(
          switchMap((res) => {
            const retried = req.clone({
              setHeaders: {
                Authorization: `Bearer ${res.accessToken}`,
                ...(userId !== null ? { 'X-User-Id': String(userId) } : {}),
              },
              withCredentials: true,
            });
            return next(retried);
          }),
          catchError((refreshError) => {
            router.navigateByUrl('/login');
            return throwError(() => refreshError);
          })
        );
      }
      return throwError(() => error);
    })
  );
};
