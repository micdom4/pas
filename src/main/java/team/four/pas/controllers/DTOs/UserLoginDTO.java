package team.four.pas.controllers.DTOs;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record UserLoginDTO(

        @NotNull(message = "login can't be null")
        @Pattern(regexp = "^[A-Z][A-Z][a-z]{1,18}[0-9]{0,5}$", message = "Wrong format of login")
        String login,

        @NotNull(message = "password can't be null")
        String password
) { }
