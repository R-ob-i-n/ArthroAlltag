import { Component, input } from '@angular/core';

/** Einheitlicher Ladezustand statt reinem Text ("Lade ...") an mehreren Stellen im Code. */
@Component({
  selector: 'app-loading-spinner',
  imports: [],
  templateUrl: './loading-spinner.html'
})
export class LoadingSpinner {
  readonly text = input('Lade ...');
}
