import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { API_BASE_URL } from '../api-base-url';
import { Patient } from '../../shared/models/patient.model';

@Injectable({ providedIn: 'root' })
export class PatientService {
  private readonly http = inject(HttpClient);

  getPatients(therapeutId: string): Observable<Patient[]> {
    return this.http.get<Patient[]>(`${API_BASE_URL}/therapeuts/${therapeutId}/patients`);
  }
}
