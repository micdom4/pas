package team.four.pas.controllers.DTOs;

import jakarta.validation.constraints.Pattern;
import org.springframework.lang.NonNull;

public record UserModDTO(@NonNull
                         @Pattern(regexp = "^[A-Z][a-z]{1,19}(-[A-Z][a-z]{1,19})?$", message = "Wrong format of surname")
                         String surname) {}

