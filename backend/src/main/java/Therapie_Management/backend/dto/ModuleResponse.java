package Therapie_Management.backend.dto;

public record ModuleResponse(
    Integer id,
    String name,
    Integer reihenfolge,
    String beschreibung
) {
}
