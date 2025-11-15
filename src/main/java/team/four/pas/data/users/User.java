package team.four.pas.data.users;

import lombok.*;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import team.four.pas.data.IdentifiableEntity;

import java.util.UUID;

@Getter
@ToString
public abstract class User extends IdentifiableEntity {
    private String login;
    private String name;
    @Setter private String surname;

    public User(UUID id, String login, String name, String surname) {
        super(id);
        this.login = login;
        this.name = name;
        this.surname = surname;
    }

}
