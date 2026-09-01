package Therapie_Management.backend.exception;

/** Wird geworfen, wenn eine angeforderte Entity (z.B. Patient, Content) nicht existiert -> 404. */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
