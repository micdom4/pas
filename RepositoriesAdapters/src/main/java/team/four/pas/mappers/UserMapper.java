package team.four.pas.mappers;

import org.mapstruct.Mapper;
import team.four.pas.entities.users.AdminEntity;
import team.four.pas.entities.users.ClientEntity;
import team.four.pas.entities.users.ManagerEntity;
import team.four.pas.entities.users.UserEntity;
import team.four.pas.data.users.Admin;
import team.four.pas.data.users.Client;
import team.four.pas.data.users.Manager;
import team.four.pas.data.users.User;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {


    default User toDomain(UserEntity userEntity) {
        if (userEntity == null) {
            return null;
        }
        return switch (userEntity) {
            case ClientEntity c -> toClient(c);
            case ManagerEntity m -> toManager(m);
            case AdminEntity a -> toAdmin(a);
            default -> throw new IllegalArgumentException("Unknown entity type: " + userEntity.getClass());
        };
    }

    default UserEntity toEntity(User user) {
        if (user == null) {
            return null;
        }
        return switch (user) {
            case Client c -> toClientEntity(c);
            case Manager m -> toManagerEntity(m);
            case Admin a -> toAdminEntity(a);
            default -> throw new IllegalArgumentException("Unknown domain type: " + user.getClass());
        };
    }

    Client toClient(ClientEntity entity);
    Manager toManager(ManagerEntity entity);
    Admin toAdmin(AdminEntity entity);

    ClientEntity toClientEntity(Client domain);
    ManagerEntity toManagerEntity(Manager domain);
    AdminEntity toAdminEntity(Admin domain);

    List<User> toDomain(List<UserEntity> entities);

    List<UserEntity> toEntity(List<User> users);
}