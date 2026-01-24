package team.four.pas.controllers.DTOs;

import jakarta.validation.constraints.Pattern;
import org.springframework.lang.Nullable;

public record UserModDTO(@Nullable
                         @Pattern(regexp = "^[A-Z][a-z]{1,19}(-[A-Z][a-z]{1,19})?$", message = "Wrong format of surname")
                         String surname, String Password) {}

