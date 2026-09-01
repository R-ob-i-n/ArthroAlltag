import { Routes } from '@angular/router';

import { ownResourceGuard } from '../../core/guards/own-resource-guard';

export const patientRoutes: Routes = [
  {
    path: ':patientId',
    canActivate: [ownResourceGuard('PATIENT', 'patientId')],
    loadComponent: () => import('./patient-home/patient-home').then((module) => module.PatientHome)
  }
];
