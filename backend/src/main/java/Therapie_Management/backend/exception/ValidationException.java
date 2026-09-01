package Therapie_Management.backend.exception;

/** Fachliche Regel verletzt (z.B. Tipp bereits favorisiert, unbekanntes ID-Praefix) -> 400. */
public class ValidationException extends RuntimeException {

    public ValidationException(String message) {
        super(message);
    }
}
