import { TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';

import { API_BASE_URL } from '../api-base-url';
import { FavoriteService } from './favorite';

/**
 * Prueft nur den HTTP-Vertrag (Methode, URL, Body) gegen einen gemockten Backend statt gegen
 * ein echtes Backend - so bleibt der Test schnell und unabhaengig davon, ob der Server laeuft.
 */
describe('FavoriteService', () => {
  let service: FavoriteService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()]
    });
    service = TestBed.inject(FavoriteService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('getFavorites ruft GET auf /patients/{id}/favorites auf', () => {
    service.getFavorites('v110001').subscribe();

    const request = httpMock.expectOne(`${API_BASE_URL}/patients/v110001/favorites`);
    expect(request.request.method).toBe('GET');
    request.flush([]);
  });

  it('addFavorite schickt POST mit tippId im Body', () => {
    service.addFavorite('v110001', 5).subscribe();

    const request = httpMock.expectOne(`${API_BASE_URL}/patients/v110001/favorites`);
    expect(request.request.method).toBe('POST');
    expect(request.request.body).toEqual({ tippId: 5 });
    request.flush({ id: 1, tippId: 5, hinzugefuegtAm: '2026-01-01T00:00:00' });
  });

  it('removeFavorite schickt DELETE an die Favoriten-Id', () => {
    service.removeFavorite('v110001', 7).subscribe();

    const request = httpMock.expectOne(`${API_BASE_URL}/patients/v110001/favorites/7`);
    expect(request.request.method).toBe('DELETE');
    request.flush(null);
  });
});
