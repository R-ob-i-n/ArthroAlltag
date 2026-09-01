package Therapie_Management.backend.exception;

/** Nutzer ist zwar eingeloggt, darf aber auf diese Ressource nicht zugreifen -> 403. */
public class ForbiddenException extends RuntimeException {

    public ForbiddenException(String message) {
        super(message);
    }
}
