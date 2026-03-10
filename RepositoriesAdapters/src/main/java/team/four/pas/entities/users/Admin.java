package team.four.pas.entities.users;

import org.springframework.data.annotation.TypeAlias;

@TypeAlias("ADMIN")
public class Admin extends UserEntity {

    public Admin(String id, String login,
                 String password, String name, String surname,
                 boolean active) {
        super(id, login, password, name, surname, active);
    }
}
