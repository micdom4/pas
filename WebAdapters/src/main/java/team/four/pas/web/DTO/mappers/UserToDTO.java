package team.four.pas.web.DTO.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import team.four.pas.data.users.Admin;
import team.four.pas.data.users.Client;
import team.four.pas.data.users.Manager;
import team.four.pas.data.users.User;
import team.four.pas.web.DTO.UserAddDTO;
import team.four.pas.web.DTO.UserDTO;

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

    Client clientFromClientDTO(UserDTO clientDto);

    Manager managerFromUserDTO(UserDTO userDto);

    Admin adminFromUserDTO(UserDTO userDto);

    @Mapping(target = "active", constant = "true")
    Client clientFromClientDTO(UserAddDTO clientDto);

    @Mapping(target = "active", constant = "true")
    Manager managerFromUserDTO(UserAddDTO userDto);

    @Mapping(target = "active", constant = "true")
    Admin adminFromUserDTO(UserAddDTO userDto);

    default List<UserDTO> toDataList(List<User> users) {
        if (users == null) {
            return null;
        }
        return users.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    default User toData(UserDTO dto) {
        if (dto == null) {
            return null;
        }

        return switch (dto.type()) {
            case CLIENT -> clientFromClientDTO(dto);
            case MANAGER -> managerFromUserDTO(dto);
            case ADMIN -> adminFromUserDTO(dto);
        };
    }

    default User toData(UserAddDTO dto) {
        if (dto == null) {
            return null;
        }

        return switch (dto.type()) {
            case CLIENT -> clientFromClientDTO(dto);
            case MANAGER -> managerFromUserDTO(dto);
            case ADMIN -> adminFromUserDTO(dto);
        };
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
