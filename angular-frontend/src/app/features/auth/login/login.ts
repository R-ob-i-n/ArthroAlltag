import { Component, inject, signal } from '@angular/core';
import { Router } from '@angular/router';

import { AuthService } from '../../../core/services/auth';
import { ErrorResponse } from '../../../shared/models/error-response.model';
import { ErrorMessage } from '../../../shared/components/error-message/error-message';

@Component({
  selector: 'app-login',
  imports: [ErrorMessage],
  templateUrl: './login.html'
})
export class Login {
  private readonly authService = inject(AuthService);
  private readonly router = inject(Router);

  protected readonly userId = signal('');
  protected readonly password = signal('');
  protected readonly isLoading = signal(false);
  protected readonly errorMessage = signal<string | null>(null);

  protected onUserIdInput(event: Event): void {
    this.userId.set((event.target as HTMLInputElement).value);
  }

  protected onPasswordInput(event: Event): void {
    this.password.set((event.target as HTMLInputElement).value);
  }

  protected onSubmit(): void {
    this.errorMessage.set(null);
    this.isLoading.set(true);

    this.authService.login({ userId: this.userId(), password: this.password() }).subscribe({
      next: (response) => {
        this.isLoading.set(false);
        const basePath = response.role === 'PATIENT' ? '/patient' : '/therapeut';
        this.router.navigateByUrl(`${basePath}/${response.userId}`);
      },
      error: (error: { error?: ErrorResponse }) => {
        this.isLoading.set(false);
        this.errorMessage.set(error.error?.message ?? 'Login fehlgeschlagen. Bitte ID und Passwort pruefen.');
      }
    });
  }
}
