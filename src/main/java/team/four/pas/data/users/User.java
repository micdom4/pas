package team.four.pas.data.users;

import lombok.*;
import team.four.pas.data.IdentifiableEntity;

import java.util.UUID;

@Getter
@ToString
public abstract class User extends IdentifiableEntity {
    private final String login;
    private final String name;
    @Setter private String surname;
    @Setter private boolean active;

    public User(UUID id, String login, String name, String surname) {
        super(id);
        this.login = login;
        this.name = name;
        this.surname = surname;
    }

}
