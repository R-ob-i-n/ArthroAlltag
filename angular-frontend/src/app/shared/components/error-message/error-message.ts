import { Component, input } from '@angular/core';

/**
 * Einheitliche Darstellung fuer alle Fehlermeldungen aus `ErrorResponse.message`
 * (Login, Tipp-Liste, Formulare, ...), statt denselben Absatz in jeder Komponente zu wiederholen.
 */
@Component({
  selector: 'app-error-message',
  imports: [],
  templateUrl: './error-message.html'
})
export class ErrorMessage {
  readonly message = input<string | null>(null);
}
