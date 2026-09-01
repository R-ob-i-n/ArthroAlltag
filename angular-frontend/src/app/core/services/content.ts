import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

import { API_BASE_URL } from '../api-base-url';
import { Content, ContentCreateRequest } from '../../shared/models/content.model';

@Injectable({ providedIn: 'root' })
export class ContentService {
  private readonly http = inject(HttpClient);

  getContents(patientId: string, categoryId?: number | null): Observable<Content[]> {
    return this.http.get<Content[]>(`${API_BASE_URL}/patients/${patientId}/contents`, {
      params: this.categoryParams(categoryId)
    });
  }

  // Rollenunabhaengiger Lesezugriff (/api/contents) - genutzt von der Therapeuten-Verwaltung,
  // die alle Tipps sehen muss und nicht nur die eines einzelnen Patienten.
  getAllContents(categoryId?: number | null): Observable<Content[]> {
    return this.http.get<Content[]>(`${API_BASE_URL}/contents`, { params: this.categoryParams(categoryId) });
  }

  createContent(therapeutId: string, request: ContentCreateRequest): Observable<Content> {
    return this.http.post<Content>(`${API_BASE_URL}/therapeuts/${therapeutId}/contents`, request);
  }

  updateContent(therapeutId: string, contentId: number, request: ContentCreateRequest): Observable<Content> {
    return this.http.put<Content>(`${API_BASE_URL}/therapeuts/${therapeutId}/contents/${contentId}`, request);
  }

  deleteContent(therapeutId: string, contentId: number): Observable<void> {
    return this.http.delete<void>(`${API_BASE_URL}/therapeuts/${therapeutId}/contents/${contentId}`);
  }

  private categoryParams(categoryId?: number | null): HttpParams {
    let params = new HttpParams();
    if (categoryId != null) {
      params = params.set('categoryId', categoryId);
    }
    return params;
  }
}
