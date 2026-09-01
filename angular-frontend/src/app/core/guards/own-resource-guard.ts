import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';

import { AuthService } from '../services/auth';
import { Role } from '../../shared/models/auth.model';

/**
 * Verhindert im Frontend, dass z.B. Patient v110001 auf /patient/v110002 navigiert.
 * Das ist reiner UX-Komfort - die eigentliche Absicherung passiert im Backend ueber
 * @PreAuthorize, das Frontend koennte jederzeit umgangen werden (DevTools, curl, ...).
 */
export function ownResourceGuard(role: Role, idParam: string): CanActivateFn {
  return (route) => {
    const authService = inject(AuthService);
    const router = inject(Router);
    const currentUser = authService.currentUser();

    if (!currentUser) {
      return router.parseUrl('/login');
    }

    if (currentUser.role !== role) {
      return router.parseUrl('/login');
    }

    const requestedId = route.paramMap.get(idParam);
    if (requestedId !== currentUser.userId) {
      const ownBasePath = role === 'PATIENT' ? '/patient' : '/therapeut';
      return router.parseUrl(`${ownBasePath}/${currentUser.userId}`);
    }

    return true;
  };
}
