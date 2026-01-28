package team.four.pas.controllers.DTOs;

public record UserDTO(
        String id,
        String login,
        String name,
        String surname,
        UserType type,
        boolean active
) { }
