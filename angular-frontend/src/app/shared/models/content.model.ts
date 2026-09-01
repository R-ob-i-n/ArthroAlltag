import { Category } from './category.model';

export type Schwierigkeitsgrad = 'LEICHT' | 'MITTEL' | 'SCHWER';

export interface Content {
  id: number;
  titel: string;
  beschreibung: string;
  kategorie: Category;
  bildUrl: string | null;
  schwierigkeitsgrad: Schwierigkeitsgrad | null;
  hilfsmittel: string | null;
  erstelltDurchName: string;
  erstelltAm: string;
  aktualisiertAm: string;
}

export interface ContentCreateRequest {
  titel: string;
  beschreibung: string;
  kategorieId: number;
  bildUrl?: string | null;
  schwierigkeitsgrad?: Schwierigkeitsgrad | null;
  hilfsmittel?: string | null;
}
