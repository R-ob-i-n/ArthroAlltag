import { Component, computed, inject, signal } from '@angular/core';

import { AuthService } from '../../../core/services/auth';
import { CategoryService } from '../../../core/services/category';
import { ContentService } from '../../../core/services/content';
import { FavoriteService } from '../../../core/services/favorite';
import { ModulService } from '../../../core/services/modul';
import { Category } from '../../../shared/models/category.model';
import { Content } from '../../../shared/models/content.model';
import { Favorite } from '../../../shared/models/favorite.model';
import { Modul } from '../../../shared/models/modul.model';
import { CategoryFilter } from '../../../shared/components/category-filter/category-filter';
import { ErrorMessage } from '../../../shared/components/error-message/error-message';
import { LoadingSpinner } from '../../../shared/components/loading-spinner/loading-spinner';

@Component({
  selector: 'app-patient-home',
  imports: [CategoryFilter, ErrorMessage, LoadingSpinner],
  templateUrl: './patient-home.html'
})
export class PatientHome {
  protected readonly authService = inject(AuthService);
  private readonly categoryService = inject(CategoryService);
  private readonly contentService = inject(ContentService);
  private readonly favoriteService = inject(FavoriteService);
  private readonly modulService = inject(ModulService);

  protected readonly categories = signal<Category[]>([]);
  protected readonly contents = signal<Content[]>([]);
  protected readonly favorites = signal<Favorite[]>([]);
  protected readonly modules = signal<Modul[]>([]);
  protected readonly selectedCategoryId = signal<number | null>(null);
  protected readonly searchTerm = signal('');
  protected readonly showFavoritesOnly = signal(false);
  protected readonly isLoading = signal(true);
  protected readonly isLoadingModules = signal(true);
  protected readonly errorMessage = signal<string | null>(null);

  // Map von Content-Id auf Favorit-Id: fuer die Anzeige (ist ein Tipp favorisiert?) und
  // zum Entfernen (die DELETE-Route braucht die Favorit-Id, nicht die Content-Id).
  private readonly favoriteIdByContentId = computed(
    () => new Map(this.favorites().map((favorite) => [favorite.tippId, favorite.id]))
  );

  // Kategorie-Filter passiert im Backend (eigener Request je Kategorie), Textsuche und
  // Favoriten-Filter dagegen rein clientseitig ueber die bereits geladenen Tipps - fuers
  // MVP reicht das, ein eigener Such-Endpoint waere fuer diese Datenmenge unnoetiger Aufwand.
  protected readonly filteredContents = computed(() => {
    const term = this.searchTerm().trim().toLowerCase();
    const favoriteIds = this.favoriteIdByContentId();

    return this.contents().filter((content) => {
      const matchesSearch =
        !term || content.titel.toLowerCase().includes(term) || content.beschreibung.toLowerCase().includes(term);
      const matchesFavorite = !this.showFavoritesOnly() || favoriteIds.has(content.id);
      return matchesSearch && matchesFavorite;
    });
  });

  constructor() {
    this.categoryService.getCategories().subscribe({
      next: (categories) => this.categories.set(categories),
      error: () => this.errorMessage.set('Kategorien konnten nicht geladen werden.')
    });
    this.loadFavorites();
    this.loadContents();
    this.loadModules();
  }

  protected onSearchInput(event: Event): void {
    this.searchTerm.set((event.target as HTMLInputElement).value);
  }

  protected selectCategory(categoryId: number | null): void {
    if (categoryId === this.selectedCategoryId()) {
      return;
    }
    this.selectedCategoryId.set(categoryId);
    this.loadContents();
  }

  protected toggleFavoritesOnly(): void {
    this.showFavoritesOnly.update((value) => !value);
  }

  protected isFavorite(contentId: number): boolean {
    return this.favoriteIdByContentId().has(contentId);
  }

  protected toggleFavorite(content: Content): void {
    const patientId = this.authService.currentUser()?.userId;
    if (!patientId) {
      return;
    }

    const favoriteId = this.favoriteIdByContentId().get(content.id);

    if (favoriteId != null) {
      this.favoriteService.removeFavorite(patientId, favoriteId).subscribe({
        next: () => this.favorites.update((favorites) => favorites.filter((favorite) => favorite.id !== favoriteId)),
        error: () => this.errorMessage.set('Favorit konnte nicht entfernt werden.')
      });
    } else {
      this.favoriteService.addFavorite(patientId, content.id).subscribe({
        next: (favorite) => this.favorites.update((favorites) => [...favorites, favorite]),
        error: () => this.errorMessage.set('Favorit konnte nicht gespeichert werden.')
      });
    }
  }

  private loadFavorites(): void {
    const patientId = this.authService.currentUser()?.userId;
    if (!patientId) {
      return;
    }

    this.favoriteService.getFavorites(patientId).subscribe({
      next: (favorites) => this.favorites.set(favorites),
      error: () => this.errorMessage.set('Favoriten konnten nicht geladen werden.')
    });
  }

  private loadModules(): void {
    const patientId = this.authService.currentUser()?.userId;
    if (!patientId) {
      return;
    }

    this.modulService.getUnlockedModules(patientId).subscribe({
      next: (modules) => {
        this.modules.set(modules);
        this.isLoadingModules.set(false);
      },
      error: () => {
        this.errorMessage.set('Module konnten nicht geladen werden.');
        this.isLoadingModules.set(false);
      }
    });
  }

  private loadContents(): void {
    const patientId = this.authService.currentUser()?.userId;
    if (!patientId) {
      return;
    }

    this.isLoading.set(true);
    this.errorMessage.set(null);

    this.contentService.getContents(patientId, this.selectedCategoryId()).subscribe({
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
