package team.four.pas.data.users;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;


@Getter
public class Client extends User {
    @Setter private boolean active;

    public Client(UUID id, String login, String name, String surname) {
        super(id, login, name, surname);
    }
}
