package team.four.pas.services.data.users;

import lombok.*;
import team.four.pas.services.data.IdentifiableObject;


@Getter @ToString
public abstract class User extends IdentifiableObject {
    private final String login;
    private final String name;
    private String surname;
    private boolean active;

    public User(String id, String login, String name, String surname, boolean active) {
        super(id);
        this.login = login;
        this.name = name;
        this.surname = surname;
        this.active = active;
    }

}
