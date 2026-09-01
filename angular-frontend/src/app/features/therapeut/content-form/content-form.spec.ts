import { ComponentFixture, TestBed } from '@angular/core/testing';

import { Category } from '../../../shared/models/category.model';
import { Content, ContentCreateRequest } from '../../../shared/models/content.model';
import { ContentForm } from './content-form';

/**
 * Testet ContentForm ueber echte DOM-Interaktion (Eingaben tippen, Formular absenden) statt
 * auf protected Methoden/Signals zuzugreifen - so wird genau das geprueft, was ein Nutzer auch
 * wirklich tut, und der Test bleibt stabil, auch wenn interne Implementierungsdetails sich
 * aendern.
 */
describe('ContentForm', () => {
  const kategorien: Category[] = [{ id: 1, name: 'Kueche' }];

  beforeEach(() => {
    TestBed.configureTestingModule({ imports: [ContentForm] });
  });

  function createFixture(): ComponentFixture<ContentForm> {
    const fixture = TestBed.createComponent(ContentForm);
    fixture.componentRef.setInput('categories', kategorien);
    fixture.detectChanges();
    return fixture;
  }

  function setValue(fixture: ComponentFixture<ContentForm>, selector: string, value: string): void {
    const element: HTMLInputElement | HTMLTextAreaElement | HTMLSelectElement = fixture.nativeElement.querySelector(selector);
    element.value = value;
    element.dispatchEvent(new Event(selector === '#kategorie' ? 'change' : 'input'));
    fixture.detectChanges();
  }

  it('Speichern-Button bleibt deaktiviert, solange Pflichtfelder fehlen', () => {
    const fixture = createFixture();
    const button: HTMLButtonElement = fixture.nativeElement.querySelector('button[type="submit"]');

    expect(button.disabled).toBe(true);

    setValue(fixture, '#titel', 'Neuer Tipp');
    setValue(fixture, '#beschreibung', 'Beschreibung');
    expect(button.disabled).toBe(true);

    setValue(fixture, '#kategorie', '1');
    expect(button.disabled).toBe(false);
  });

  it('emittiert save mit den eingegebenen Werten beim Absenden', () => {
    const fixture = createFixture();
    let emitted: ContentCreateRequest | undefined;
    fixture.componentInstance.save.subscribe((value) => (emitted = value));

    setValue(fixture, '#titel', 'Neuer Tipp');
    setValue(fixture, '#beschreibung', 'Beschreibung');
    setValue(fixture, '#kategorie', '1');
    fixture.nativeElement.querySelector('form').dispatchEvent(new Event('submit'));

    expect(emitted).toEqual({
      titel: 'Neuer Tipp',
      beschreibung: 'Beschreibung',
      kategorieId: 1,
      bildUrl: null,
      schwierigkeitsgrad: null,
      hilfsmittel: null
    });
  });

  it('befuellt die Felder beim Bearbeiten eines vorhandenen Tipps vor', () => {
    const vorhandenerTipp: Content = {
      id: 5,
      titel: 'Vorhandener Tipp',
      beschreibung: 'Vorhandene Beschreibung',
      kategorie: kategorien[0],
      bildUrl: null,
      schwierigkeitsgrad: null,
      hilfsmittel: null,
      erstelltDurchName: 'Maren Klein',
      erstelltAm: '2026-01-01T00:00:00',
      aktualisiertAm: '2026-01-01T00:00:00'
    };

    const fixture = createFixture();
    fixture.componentRef.setInput('content', vorhandenerTipp);
    fixture.detectChanges();

    const titelInput: HTMLInputElement = fixture.nativeElement.querySelector('#titel');
    expect(titelInput.value).toBe('Vorhandener Tipp');
  });
});
