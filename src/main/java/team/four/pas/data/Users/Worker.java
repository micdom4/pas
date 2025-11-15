package team.four.pas.data.Users;

import java.util.UUID;

public class Worker extends User {
    public Worker(UUID id, String login, String name, String surname, boolean active) {
        super(id, login, name, surname, active);
    }
}
