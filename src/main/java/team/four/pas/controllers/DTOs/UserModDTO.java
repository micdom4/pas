package team.four.pas.controllers.DTOs;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record UserModDTO(@NotNull(message = "surname can't be null")
                         @Pattern(regexp = "^[A-Z][a-z]{1,19}(-[A-Z][a-z]{1,19})?$", message = "Wrong format of surname")
                         String surname) {}

