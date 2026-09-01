package Therapie_Management.backend.dto;

import Therapie_Management.backend.entity.Schwierigkeitsgrad;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Wird sowohl für POST (Anlegen) als auch PUT (Bearbeiten) eines Tipps verwendet,
 * da beide Operationen dieselben Felder benötigen.
 */
public record ContentCreateRequest(
    @NotBlank(message = "Titel darf nicht leer sein") String titel,
    @NotBlank(message = "Beschreibung darf nicht leer sein") String beschreibung,
    @NotNull(message = "Kategorie muss angegeben werden") Integer kategorieId,
    String bildUrl,
    Schwierigkeitsgrad schwierigkeitsgrad,
    String hilfsmittel
) {
}
