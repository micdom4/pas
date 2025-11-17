package team.four.pas.controllers.DTOs;


public record UserAddDTO(
        String login,
        String name,
        String surname,
        UserType type
) { }
