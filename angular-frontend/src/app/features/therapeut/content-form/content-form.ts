import { Component, computed, effect, input, output, signal } from '@angular/core';

import { Category } from '../../../shared/models/category.model';
import { Content, ContentCreateRequest, Schwierigkeitsgrad } from '../../../shared/models/content.model';
import { ErrorMessage } from '../../../shared/components/error-message/error-message';

/**
 * Ein Formular fuer Anlegen UND Bearbeiten: `content` ist null beim Anlegen, sonst der zu
 * bearbeitende Tipp. So gibt es nur eine Stelle mit den Formularfeldern statt zwei fast
 * identische Komponenten.
 */
@Component({
  selector: 'app-content-form',
  imports: [ErrorMessage],
  templateUrl: './content-form.html'
})
export class ContentForm {
  readonly content = input<Content | null>(null);
  readonly categories = input.required<Category[]>();
  readonly errorMessage = input<string | null>(null);
  readonly isSaving = input(false);

  readonly save = output<ContentCreateRequest>();
  readonly cancel = output<void>();

  protected readonly titel = signal('');
  protected readonly beschreibung = signal('');
  protected readonly kategorieId = signal<number | null>(null);
  protected readonly bildUrl = signal('');
  protected readonly schwierigkeitsgrad = signal<Schwierigkeitsgrad | ''>('');
  protected readonly hilfsmittel = signal('');

  protected readonly isValid = computed(() => this.titel().trim() !== '' && this.beschreibung().trim() !== '' && this.kategorieId() != null);

  constructor() {
    // Laeuft bei jedem Wechsel von `content` (neuer Tipp <-> vorhandenen Tipp bearbeiten)
    // und setzt die Felder entsprechend zurueck bzw. befuellt sie vor.
    effect(() => {
      const current = this.content();
      this.titel.set(current?.titel ?? '');
      this.beschreibung.set(current?.beschreibung ?? '');
      this.kategorieId.set(current?.kategorie.id ?? null);
      this.bildUrl.set(current?.bildUrl ?? '');
      this.schwierigkeitsgrad.set(current?.schwierigkeitsgrad ?? '');
      this.hilfsmittel.set(current?.hilfsmittel ?? '');
    });
  }

  protected onTitelInput(event: Event): void {
    this.titel.set((event.target as HTMLInputElement).value);
  }

  protected onBeschreibungInput(event: Event): void {
    this.beschreibung.set((event.target as HTMLTextAreaElement).value);
  }

  protected onKategorieChange(event: Event): void {
    const value = (event.target as HTMLSelectElement).value;
    this.kategorieId.set(value === '' ? null : Number(value));
  }

  protected onBildUrlInput(event: Event): void {
    this.bildUrl.set((event.target as HTMLInputElement).value);
  }

  protected onSchwierigkeitsgradChange(event: Event): void {
    this.schwierigkeitsgrad.set((event.target as HTMLSelectElement).value as Schwierigkeitsgrad | '');
  }

  protected onHilfsmittelInput(event: Event): void {
    this.hilfsmittel.set((event.target as HTMLInputElement).value);
  }

  protected onSubmit(): void {
    if (!this.isValid()) {
      return;
    }

    this.save.emit({
      titel: this.titel().trim(),
      beschreibung: this.beschreibung().trim(),
      kategorieId: this.kategorieId()!,
      bildUrl: this.bildUrl().trim() || null,
      schwierigkeitsgrad: this.schwierigkeitsgrad() || null,
      hilfsmittel: this.hilfsmittel().trim() || null
    });
  }
}
