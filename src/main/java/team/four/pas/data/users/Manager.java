package team.four.pas.data.users;

import java.util.UUID;

public class Manager extends User {
    public Manager(UUID id, String login, String name, String surname, boolean active) {
        super(id, login, name, surname, active);
    }
}
