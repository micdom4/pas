package team.four.pas.services.data.users;

import lombok.Getter;

import java.util.UUID;


@Getter
public class Client extends User {
    public Client(String id, String login, String name, String surname) {
        super(id, login, name, surname);
    }
}
