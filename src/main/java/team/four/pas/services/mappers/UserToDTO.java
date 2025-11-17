package team.four.pas.services.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import team.four.pas.controllers.DTOs.UserDTO;
import team.four.pas.services.data.users.Admin;
import team.four.pas.services.data.users.Client;
import team.four.pas.services.data.users.Manager;
import team.four.pas.services.data.users.User;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface UserToDTO {

    @Mapping(target = "type", constant = "CLIENT")
    UserDTO dtoFromClient(Client client);

    @Mapping(target = "type", constant = "MANAGER")
    UserDTO dtoFromManager(Manager manager);

    @Mapping(target = "type", constant = "ADMIN")
    UserDTO dtoFromAdmin(Admin admin);

    default List<UserDTO> toDataList(List<User> users) {
        if (users == null) {
            return null;
        }
        return  users.stream()
                       .map(this::toDTO)
                       .collect(Collectors.toList());
    }

    default UserDTO toDTO(User data) {
        if (data == null) {
            return null;
        }

        return switch (data) {
            case Client client -> dtoFromClient(client);
            case Admin admin -> dtoFromAdmin(admin);
            case Manager manager -> dtoFromManager(manager);
            default -> throw new IllegalArgumentException("Unknown user type: " + data.getClass());
        };

    }

}
