package Therapie_Management.backend.dto;

import jakarta.validation.constraints.NotNull;

public record FavoriteCreateRequest(
    @NotNull(message = "tippId muss angegeben werden") Integer tippId
) {
}
