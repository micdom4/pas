package team.four.pas.DTO;

import jakarta.validation.constraints.NotNull;

public record AllocationAddDTO(
        @NotNull
        String clientId,
        @NotNull
        String resourceId
) {
}
