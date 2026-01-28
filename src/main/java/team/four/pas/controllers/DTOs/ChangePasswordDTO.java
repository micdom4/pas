package team.four.pas.controllers.DTOs;
import jakarta.validation.constraints.NotNull;

public record ChangePasswordDTO(@NotNull String oldPassword,
                                @NotNull String newPassword ) {
}
