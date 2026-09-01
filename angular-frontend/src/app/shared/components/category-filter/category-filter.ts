import { Component, input, output } from '@angular/core';

import { Category } from '../../models/category.model';

/**
 * `host: { style: 'display: contents' }` macht das Host-Element unsichtbar fuers Layout, damit
 * die Buttons direkt im `flex flex-wrap`-Filter-Container des Elternteils mitlaufen - so
 * koennen patient-home und therapeut-home eigene Filter-Chips (z.B. "Nur Favoriten") in
 * derselben Zeile daneben anzeigen, statt dass diese Komponente einen eigenen Block bildet.
 */
@Component({
  selector: 'app-category-filter',
  imports: [],
  templateUrl: './category-filter.html',
  host: { style: 'display: contents' }
})
export class CategoryFilter {
  readonly categories = input.required<Category[]>();
  readonly selectedCategoryId = input<number | null>(null);

  readonly select = output<number | null>();
}
