import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { API_BASE_URL } from '../api-base-url';
import { Favorite, FavoriteCreateRequest } from '../../shared/models/favorite.model';

@Injectable({ providedIn: 'root' })
export class FavoriteService {
  private readonly http = inject(HttpClient);

  getFavorites(patientId: string): Observable<Favorite[]> {
    return this.http.get<Favorite[]>(`${API_BASE_URL}/patients/${patientId}/favorites`);
  }

  addFavorite(patientId: string, tippId: number): Observable<Favorite> {
    const request: FavoriteCreateRequest = { tippId };
    return this.http.post<Favorite>(`${API_BASE_URL}/patients/${patientId}/favorites`, request);
  }

  removeFavorite(patientId: string, favoriteId: number): Observable<void> {
    return this.http.delete<void>(`${API_BASE_URL}/patients/${patientId}/favorites/${favoriteId}`);
  }
}
