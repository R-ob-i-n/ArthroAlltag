package Therapie_Management.backend.dto;

public record PatientResponse(
    String id,
    String vorname,
    String nachname,
    String diagnostik
) {
}
