package team.four.pas.entities.users;


import org.springframework.data.annotation.TypeAlias;

@TypeAlias("MANAGER")
public class Manager extends UserEntity {

    public Manager(String id, String login,
                   String password, String name,
                   String surname, boolean active) {
        super(id, login, password, name, surname, active);
    }
}
