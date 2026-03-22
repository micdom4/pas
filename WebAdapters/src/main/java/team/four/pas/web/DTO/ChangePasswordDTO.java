package team.four.pas.web.DTO;
import jakarta.validation.constraints.NotNull;

public record ChangePasswordDTO(@NotNull String oldPassword,
                                @NotNull String newPassword ) {
}
