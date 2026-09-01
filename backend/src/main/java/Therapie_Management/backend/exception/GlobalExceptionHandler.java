package Therapie_Management.backend.exception;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import Therapie_Management.backend.dto.ErrorResponse;

/**
 * Faengt alle Exceptions aus Controllern (und aus der Security-Method-Interception, die
 * beim Dispatch ausgefuehrt wird) an einer Stelle ab und uebersetzt sie in einheitliche
 * JSON-Fehlerantworten mit passendem HTTP-Status.
 *
 * AccessDeniedException landet hier, weil @PreAuthorize (SecurityConfig/PatientController/
 * TherapeutController) erst beim Aufruf der Controller-Methode ausgewertet wird - anders als
 * "kein/ungueltiges Token", das schon vorher im JwtAuthenticationFilter scheitert und deshalb
 * weiterhin ueber den authenticationEntryPoint in SecurityConfig laeuft.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFound(ResourceNotFoundException exception, WebRequest request) {
        return buildResponse(HttpStatus.NOT_FOUND, exception.getMessage(), request);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorResponse> handleUnauthorized(UnauthorizedException exception, WebRequest request) {
        return buildResponse(HttpStatus.UNAUTHORIZED, exception.getMessage(), request);
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ErrorResponse> handleForbidden(ForbiddenException exception, WebRequest request) {
        return buildResponse(HttpStatus.FORBIDDEN, exception.getMessage(), request);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorResponse> handleValidation(ValidationException exception, WebRequest request) {
        return buildResponse(HttpStatus.BAD_REQUEST, exception.getMessage(), request);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDenied(AccessDeniedException exception, WebRequest request) {
        return buildResponse(HttpStatus.FORBIDDEN, "Zugriff nicht erlaubt", request);
    }

    /** Wird ausgeloest, wenn @Valid an einem @RequestBody-DTO fehlschlaegt (z.B. leerer Titel). */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationErrors(MethodArgumentNotValidException exception,
                                                                  WebRequest request) {
        String message = exception.getBindingResult().getFieldErrors().stream()
            .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
            .collect(Collectors.joining("; "));
        return buildResponse(HttpStatus.BAD_REQUEST, message, request);
    }

    /** Auffangnetz fuer alles Unerwartete - verhindert, dass Stacktraces an den Client gehen. */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpected(Exception exception, WebRequest request) {
        log.error("Unerwarteter Fehler", exception);
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Unerwarteter Fehler", request);
    }

    private ResponseEntity<ErrorResponse> buildResponse(HttpStatus status, String message, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(
            LocalDateTime.now(),
            status.value(),
            status.getReasonPhrase(),
            message,
            request.getDescription(false).replace("uri=", "")
        );
        return ResponseEntity.status(status).body(errorResponse);
    }
}
