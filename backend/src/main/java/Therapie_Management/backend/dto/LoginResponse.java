package Therapie_Management.backend.dto;

/**
 * role wird als String übertragen (statt als Role-Enum), damit das Frontend
 * nicht auf den Backend-Enum-Typ angewiesen ist.
 */
public record LoginResponse(
    String token,
    String userId,
    String role,
    String vorname,
    String nachname
) {
}
