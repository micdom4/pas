package team.four.pas.data.users;

import java.util.UUID;


public class Client extends User {
    public Client(UUID id, String login, String name, String surname) {
        super(id, login, name, surname);
    }
}
