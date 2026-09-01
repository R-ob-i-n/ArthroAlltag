package Therapie_Management.backend.exception;

/** Login fehlgeschlagen oder Token ungueltig -> 401. */
public class UnauthorizedException extends RuntimeException {

    public UnauthorizedException(String message) {
        super(message);
    }
}
