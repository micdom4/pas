package team.four.pas.repositories.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import team.four.pas.repositories.entities.UserEntity;
import team.four.pas.services.data.users.Admin;
import team.four.pas.services.data.users.Client;
import team.four.pas.services.data.users.Manager;
import team.four.pas.services.data.users.User;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(uses = ObjectIdStringMapper.class)
public interface UserMapper {

    Client toClient(UserEntity entity);
    Manager toManager(UserEntity entity);
    Admin toAdmin(UserEntity entity);

    @Mapping(target = "type", constant = "CLIENT")
    UserEntity entityFromClient(Client client);

    @Mapping(target = "type", constant = "MANAGER")
    UserEntity entityFromManager(Manager manager);

    @Mapping(target = "type", constant = "ADMIN")
    UserEntity entityFromAdmin(Admin admin);

    default User toData(UserEntity entity) {
        if (entity == null) {
            return null;
        }

        return switch (entity.type()) {
            case CLIENT -> toClient(entity);
            case MANAGER -> toManager(entity);
            case ADMIN -> toAdmin(entity);
            default -> throw new IllegalArgumentException("Unknown user type: " + entity.type());
        };
    }

    default List<User> toDataList(List<UserEntity> entities) {
        if (entities == null) {
            return null;
        }
        return entities.stream()
                       .map(this::toData)
                       .collect(Collectors.toList());
    }

    default UserEntity toEntity(User data) {
        if (data == null) {
            return null;
        }

        return switch (data) {
            case Client client -> entityFromClient(client);
            case Admin admin -> entityFromAdmin(admin);
            case Manager manager -> entityFromManager(manager);
            default -> throw new IllegalArgumentException("Unknown user type: " + data.getClass());
        };

    }

}
