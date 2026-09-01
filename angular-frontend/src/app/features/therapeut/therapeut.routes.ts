import { Routes } from '@angular/router';

import { ownResourceGuard } from '../../core/guards/own-resource-guard';

export const therapeutRoutes: Routes = [
  {
    path: ':therapeutId',
    canActivate: [ownResourceGuard('THERAPEUT', 'therapeutId')],
    loadComponent: () => import('./therapeut-home/therapeut-home').then((module) => module.TherapeutHome)
  }
];
