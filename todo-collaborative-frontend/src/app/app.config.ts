import { provideHttpClient, withInterceptors } from '@angular/common/http';
import { ApplicationConfig, inject, provideAppInitializer, provideZoneChangeDetection } from '@angular/core';
import { provideRouter } from '@angular/router';
import { firstValueFrom } from 'rxjs';
import { routes } from './app.routes';
import { authInterceptor } from './core/interceptors/auth.interceptor';
import { AuthService } from './core/services/auth.service';

export const appConfig: ApplicationConfig = {
  providers: [
    provideZoneChangeDetection({ eventCoalescing: true }),
    provideRouter(routes),
    provideHttpClient(withInterceptors([authInterceptor])),
    // Tente de restaurer la session via le cookie httpOnly refreshToken avant
    // que l'app ne s'affiche, pour éviter un flash "non connecté" au reload.
    // Échec silencieux si l'utilisateur n'a simplement pas de session valide.
    provideAppInitializer(() => {
      const authService = inject(AuthService);
      return firstValueFrom(authService.tryRestoreSession());
    }),
  ],
};
