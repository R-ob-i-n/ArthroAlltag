import { Component, inject, signal } from '@angular/core';

import { AuthService } from '../../../core/services/auth';
import { CategoryService } from '../../../core/services/category';
import { ContentService } from '../../../core/services/content';
import { ModulService } from '../../../core/services/modul';
import { PatientService } from '../../../core/services/patient';
import { Category } from '../../../shared/models/category.model';
import { Content, ContentCreateRequest } from '../../../shared/models/content.model';
import { ErrorResponse } from '../../../shared/models/error-response.model';
import { Modul } from '../../../shared/models/modul.model';
import { Patient } from '../../../shared/models/patient.model';
import { CategoryFilter } from '../../../shared/components/category-filter/category-filter';
import { ErrorMessage } from '../../../shared/components/error-message/error-message';
import { LoadingSpinner } from '../../../shared/components/loading-spinner/loading-spinner';
import { ContentForm } from '../content-form/content-form';

@Component({
  selector: 'app-therapeut-home',
  imports: [ContentForm, CategoryFilter, ErrorMessage, LoadingSpinner],
  templateUrl: './therapeut-home.html'
})
export class TherapeutHome {
  protected readonly authService = inject(AuthService);
  private readonly categoryService = inject(CategoryService);
  private readonly contentService = inject(ContentService);
  private readonly patientService = inject(PatientService);
  private readonly modulService = inject(ModulService);

  protected readonly categories = signal<Category[]>([]);
  protected readonly contents = signal<Content[]>([]);
  protected readonly selectedCategoryId = signal<number | null>(null);
  protected readonly isLoading = signal(true);
  protected readonly errorMessage = signal<string | null>(null);

  protected readonly showForm = signal(false);
  // null = neuen Tipp anlegen, Content = bestehenden bearbeiten - nur relevant, solange showForm true ist.
  protected readonly editingContent = signal<Content | null>(null);
  protected readonly isSaving = signal(false);
  protected readonly formErrorMessage = signal<string | null>(null);

  protected readonly patients = signal<Patient[]>([]);
  protected readonly allModules = signal<Modul[]>([]);
  protected readonly selectedPatientId = signal<string | null>(null);
  protected readonly unlockedModuleIds = signal<Set<number>>(new Set());
  protected readonly isLoadingModules = signal(false);
  protected readonly unlockingModuleId = signal<number | null>(null);
  protected readonly revokingModuleId = signal<number | null>(null);
  protected readonly moduleErrorMessage = signal<string | null>(null);

  constructor() {
    this.categoryService.getCategories().subscribe({
      next: (categories) => this.categories.set(categories),
      error: () => this.errorMessage.set('Kategorien konnten nicht geladen werden.')
    });
    this.loadContents();

    const therapeutId = this.authService.currentUser()?.userId;
    if (therapeutId) {
      this.patientService.getPatients(therapeutId).subscribe({
        next: (patients) => this.patients.set(patients),
        error: () => this.moduleErrorMessage.set('Patienten konnten nicht geladen werden.')
      });
    }
    this.modulService.getAllModules().subscribe({
      next: (modules) => this.allModules.set(modules),
      error: () => this.moduleErrorMessage.set('Module konnten nicht geladen werden.')
    });
  }

  protected selectCategory(categoryId: number | null): void {
    if (categoryId === this.selectedCategoryId()) {
      return;
    }
    this.selectedCategoryId.set(categoryId);
    this.loadContents();
  }

  protected openCreateForm(): void {
    this.formErrorMessage.set(null);
    this.editingContent.set(null);
    this.showForm.set(true);
  }

  protected openEditForm(content: Content): void {
    this.formErrorMessage.set(null);
    this.editingContent.set(content);
    this.showForm.set(true);
  }

  protected closeForm(): void {
    this.showForm.set(false);
  }

  protected onSave(request: ContentCreateRequest): void {
    const therapeutId = this.authService.currentUser()?.userId;
    if (!therapeutId) {
      return;
    }

    const editing = this.editingContent();

    this.isSaving.set(true);
    this.formErrorMessage.set(null);

    const request$ =
      editing === null
        ? this.contentService.createContent(therapeutId, request)
        : this.contentService.updateContent(therapeutId, editing.id, request);

    request$.subscribe({
      next: () => {
        this.isSaving.set(false);
        this.closeForm();
        this.loadContents();
      },
      error: (error: { error?: ErrorResponse }) => {
        this.isSaving.set(false);
        this.formErrorMessage.set(error.error?.message ?? 'Tipp konnte nicht gespeichert werden.');
      }
    });
  }

  protected onDelete(content: Content): void {
    const therapeutId = this.authService.currentUser()?.userId;
    if (!therapeutId || !confirm(`"${content.titel}" wirklich loeschen?`)) {
      return;
    }

    this.contentService.deleteContent(therapeutId, content.id).subscribe({
      next: () => this.loadContents(),
      error: () => this.errorMessage.set('Tipp konnte nicht geloescht werden.')
    });
  }

  protected onPatientSelect(event: Event): void {
    const patientId = (event.target as HTMLSelectElement).value || null;
    this.selectedPatientId.set(patientId);
    this.moduleErrorMessage.set(null);

    if (!patientId) {
      this.unlockedModuleIds.set(new Set());
      return;
    }

    const therapeutId = this.authService.currentUser()?.userId;
    if (!therapeutId) {
      return;
    }

    this.isLoadingModules.set(true);
    this.modulService.getPatientModules(therapeutId, patientId).subscribe({
      next: (modules) => {
        this.unlockedModuleIds.set(new Set(modules.map((modul) => modul.id)));
        this.isLoadingModules.set(false);
      },
      error: () => {
        this.moduleErrorMessage.set('Freischaltstatus konnte nicht geladen werden.');
        this.isLoadingModules.set(false);
      }
    });
  }

  protected isModuleUnlocked(modulId: number): boolean {
    return this.unlockedModuleIds().has(modulId);
  }

  protected unlockModule(modul: Modul): void {
    const therapeutId = this.authService.currentUser()?.userId;
    const patientId = this.selectedPatientId();
    if (!therapeutId || !patientId) {
      return;
    }

    this.unlockingModuleId.set(modul.id);
    this.moduleErrorMessage.set(null);

    this.modulService.unlockModule(therapeutId, { patientId, modulId: modul.id }).subscribe({
      next: () => {
        this.unlockedModuleIds.update((ids) => new Set(ids).add(modul.id));
        this.unlockingModuleId.set(null);
      },
      error: (error: { error?: ErrorResponse }) => {
        this.moduleErrorMessage.set(error.error?.message ?? 'Modul konnte nicht freigeschaltet werden.');
        this.unlockingModuleId.set(null);
      }
    });
  }

  protected resetModule(modul: Modul): void {
    const therapeutId = this.authService.currentUser()?.userId;
    const patientId = this.selectedPatientId();
    if (!therapeutId || !patientId || !confirm(`"${modul.name}" wirklich zuruecksetzen? Der Patient sieht das Modul danach nicht mehr.`)) {
      return;
    }

    this.revokingModuleId.set(modul.id);
    this.moduleErrorMessage.set(null);

    this.modulService.revokeModule(therapeutId, patientId, modul.id).subscribe({
      next: () => {
        this.unlockedModuleIds.update((ids) => {
          const updated = new Set(ids);
          updated.delete(modul.id);
          return updated;
        });
        this.revokingModuleId.set(null);
      },
      error: (error: { error?: ErrorResponse }) => {
        this.moduleErrorMessage.set(error.error?.message ?? 'Modul konnte nicht zurueckgesetzt werden.');
        this.revokingModuleId.set(null);
      }
    });
  }

  private loadContents(): void {
    this.isLoading.set(true);
    this.errorMessage.set(null);

    this.contentService.getAllContents(this.selectedCategoryId()).subscribe({
      next: (contents) => {
        this.contents.set(contents);
        this.isLoading.set(false);
      },
      error: () => {
        this.errorMessage.set('Tipps konnten nicht geladen werden.');
        this.isLoading.set(false);
      }
    });
  }
}
