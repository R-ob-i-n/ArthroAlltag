package Therapie_Management.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UnlockModuleRequest(
    @NotBlank(message = "patientId muss angegeben werden") String patientId,
    @NotNull(message = "modulId muss angegeben werden") Integer modulId
) {
}
