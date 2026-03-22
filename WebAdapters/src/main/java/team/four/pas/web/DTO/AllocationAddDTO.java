package team.four.pas.web.DTO;

import jakarta.validation.constraints.NotNull;

public record AllocationAddDTO(
        @NotNull
        String clientId,
        @NotNull
        String resourceId
) {
}
