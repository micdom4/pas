package team.four.pas.controllers.DTOs;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record UserAddDTO(

        @NotNull(message = "login can't be null")
        @Pattern(regexp = "^[A-Z][A-Z][a-z]{1,18}[0-9]{0,5}$", message = "Wrong format of login")
        String login,

        @NotNull(message = "name can't be null")
        @Pattern(regexp = "^[A-Z][a-z]{1,19}$", message = "Wrong format of name")
        String name,

        @NotNull(message = "surname can't be null")
        @Pattern(regexp = "^[A-Z][a-z]{1,19}(-[A-Z][a-z]{1,19})?$", message = "Wrong format of surname")
        String surname,

        UserType type
) { }
