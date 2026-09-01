package Therapie_Management.backend.dto;

import java.time.LocalDateTime;

public record FavoriteResponse(
    Integer id,
    Integer tippId,
    LocalDateTime hinzugefuegtAm
) {
}
