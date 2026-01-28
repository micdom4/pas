package team.four.pas.controllers.DTOs;

import jakarta.validation.constraints.NotNull;

public record AllocationAddDTO(
        @NotNull
        String clientId,
        @NotNull
        String resourceId
) {
}
