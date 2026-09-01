import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { Router } from '@angular/router';
import { catchError, throwError } from 'rxjs';

import { API_BASE_URL } from '../api-base-url';
import { AuthService } from '../services/auth';

const LOGIN_URL = `${API_BASE_URL}/auth/login`;

/**
 * Haengt bei jedem Request automatisch "Authorization: Bearer <token>" an, falls eingeloggt,
 * und meldet bei 401 automatisch ab (z.B. wenn ein Token manuell aus dem localStorage entfernt
 * oder manipuliert wurde). Der Login-Request selbst ist ausgenommen: ein 401 dort bedeutet
 * "falsches Passwort", nicht "Sitzung ungueltig", und wird bereits vom Login-Formular ueber
 * dessen eigene Fehlermeldung behandelt.
 */
export const authInterceptor: HttpInterceptorFn = (request, next) => {
  const authService = inject(AuthService);
  const router = inject(Router);
  const token = authService.token();

  const authorizedRequest = token
    ? request.clone({ setHeaders: { Authorization: `Bearer ${token}` } })
    : request;

  return next(authorizedRequest).pipe(
    catchError((error) => {
      if (error.status === 401 && request.url !== LOGIN_URL) {
        authService.logout();
        router.navigateByUrl('/login');
      }
      return throwError(() => error);
    })
  );
};
