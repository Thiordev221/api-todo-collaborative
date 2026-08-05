import { Routes } from '@angular/router';
import { adminGuard } from './core/guards/admin.guard';
import { authGuard } from './core/guards/auth.guard';

export const routes: Routes = [
  { path: '', redirectTo: 'lists', pathMatch: 'full' },
  {
    path: 'login',
    loadComponent: () =>
      import('./features/auth/login/login.component').then((m) => m.LoginComponent),
  },
  {
    path: 'register',
    loadComponent: () =>
      import('./features/auth/register/register.component').then(
        (m) => m.RegisterComponent
      ),
  },
  {
    path: 'lists',
    canActivate: [authGuard],
    loadComponent: () =>
      import('./features/lists/list-lists/list-lists.component').then(
        (m) => m.ListListsComponent
      ),
  },
  {
    path: 'lists/:id',
    canActivate: [authGuard],
    loadComponent: () =>
      import('./features/lists/list-detail/list-detail.component').then(
        (m) => m.ListDetailComponent
      ),
  },
  {
    path: 'admin/utilisateurs',
    canActivate: [authGuard, adminGuard],
    loadComponent: () =>
      import('./features/admin/user-management/user-management.component').then(
        (m) => m.UserManagementComponent
      ),
  },
  { path: '**', redirectTo: 'lists' },
];
