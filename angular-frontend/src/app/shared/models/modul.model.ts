export interface Modul {
  id: number;
  name: string;
  reihenfolge: number;
  beschreibung: string;
}

export interface UnlockModuleRequest {
  patientId: string;
  modulId: number;
}
