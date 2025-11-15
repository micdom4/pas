package team.four.pas.data.Users;

import java.util.UUID;

public class Administrator extends User{
    public Administrator(UUID id, String login, String name, String surname, boolean active) {
        super(id, login, name, surname, active);
    }
}
