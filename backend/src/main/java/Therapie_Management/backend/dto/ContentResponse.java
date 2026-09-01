package Therapie_Management.backend.dto;

import java.time.LocalDateTime;

import Therapie_Management.backend.entity.Schwierigkeitsgrad;

public record ContentResponse(
    Integer id,
    String titel,
    String beschreibung,
    CategoryResponse kategorie,
    String bildUrl,
    Schwierigkeitsgrad schwierigkeitsgrad,
    String hilfsmittel,
    String erstelltDurchName,
    LocalDateTime erstelltAm,
    LocalDateTime aktualisiertAm
) {
}
