import { Injectable, computed, inject, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';

import { API_BASE_URL } from '../api-base-url';
import { LoginRequest, LoginResponse } from '../../shared/models/auth.model';

const STORAGE_KEY = 'therapie-management-auth';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly http = inject(HttpClient);

  private readonly currentUserSignal = signal<LoginResponse | null>(this.loadFromStorage());

  readonly currentUser = this.currentUserSignal.asReadonly();
  readonly isLoggedIn = computed(() => this.currentUserSignal() !== null);
  readonly token = computed(() => this.currentUserSignal()?.token ?? null);

  login(request: LoginRequest): Observable<LoginResponse> {
    return this.http
      .post<LoginResponse>(`${API_BASE_URL}/auth/login`, request)
      .pipe(tap((response) => this.setCurrentUser(response)));
  }

  logout(): void {
    this.currentUserSignal.set(null);
    localStorage.removeItem(STORAGE_KEY);
  }

  private setCurrentUser(response: LoginResponse): void {
    this.currentUserSignal.set(response);
    localStorage.setItem(STORAGE_KEY, JSON.stringify(response));
  }

  private loadFromStorage(): LoginResponse | null {
    const raw = localStorage.getItem(STORAGE_KEY);
    return raw ? (JSON.parse(raw) as LoginResponse) : null;
  }
}
