package team.four.pas.entities.users;

import org.springframework.data.annotation.TypeAlias;

@TypeAlias("ADMIN")
public class AdminEntity extends UserEntity {

    public AdminEntity(String id, String login,
                       String password, String name, String surname,
                       boolean active) {
        super(id, login, password, name, surname, active);
    }
}
