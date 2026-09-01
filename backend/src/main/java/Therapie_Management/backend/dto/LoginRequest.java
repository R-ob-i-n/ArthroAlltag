package Therapie_Management.backend.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
    @NotBlank(message = "userId darf nicht leer sein") String userId,
    @NotBlank(message = "password darf nicht leer sein") String password
) {
}
