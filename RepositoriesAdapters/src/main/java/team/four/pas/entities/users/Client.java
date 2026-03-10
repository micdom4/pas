package team.four.pas.entities.users;


import org.springframework.data.annotation.TypeAlias;

@TypeAlias("CLIENT")
public class Client extends UserEntity {

    public Client(String id, String login,
                  String password, String name, String surname,
                  boolean active) {
        super(id, login, password, name, surname, active);
    }
}
