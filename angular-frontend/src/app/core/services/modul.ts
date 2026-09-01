import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { API_BASE_URL } from '../api-base-url';
import { Modul, UnlockModuleRequest } from '../../shared/models/modul.model';

@Injectable({ providedIn: 'root' })
export class ModulService {
  private readonly http = inject(HttpClient);

  getAllModules(): Observable<Modul[]> {
    return this.http.get<Modul[]>(`${API_BASE_URL}/modules`);
  }

  getUnlockedModules(patientId: string): Observable<Modul[]> {
    return this.http.get<Modul[]>(`${API_BASE_URL}/patients/${patientId}/modules`);
  }

  // Fuer die Therapeuten-Ansicht: derselbe Freischaltstatus, aber ueber die Therapeuten-Route
  // abgerufen, weil /api/patients/{id}/modules nur dem Patienten selbst erlaubt ist.
  getPatientModules(therapeutId: string, patientId: string): Observable<Modul[]> {
    return this.http.get<Modul[]>(`${API_BASE_URL}/therapeuts/${therapeutId}/patients/${patientId}/modules`);
  }

  unlockModule(therapeutId: string, request: UnlockModuleRequest): Observable<void> {
    return this.http.post<void>(`${API_BASE_URL}/therapeuts/${therapeutId}/unlock-module`, request);
  }
}
