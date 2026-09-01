package Therapie_Management.backend.dto;

import java.time.LocalDateTime;

/** Einheitliche Fehler-Antwort fuer alle vom GlobalExceptionHandler behandelten Faelle. */
public record ErrorResponse(
    LocalDateTime timestamp,
    int status,
    String error,
    String message,
    String path
) {
}
