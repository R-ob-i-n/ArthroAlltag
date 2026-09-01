import { Routes } from '@angular/router';

import { authGuard } from './core/guards/auth-guard';

export const routes: Routes = [
  { path: '', pathMatch: 'full', redirectTo: 'login' },
  {
    path: 'login',
    loadComponent: () => import('./features/auth/login/login').then((module) => module.Login)
  },
  {
    path: 'patient',
    canActivate: [authGuard],
    loadChildren: () => import('./features/patient/patient.routes').then((module) => module.patientRoutes)
  },
  {
    path: 'therapeut',
    canActivate: [authGuard],
    loadChildren: () => import('./features/therapeut/therapeut.routes').then((module) => module.therapeutRoutes)
  },
  { path: '**', redirectTo: 'login' }
];
