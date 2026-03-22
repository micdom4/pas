package team.four.pas.web.DTO;

public record UserDTO(
        String id,
        String login,
        String name,
        String surname,
        UserType type,
        boolean active
) { }
