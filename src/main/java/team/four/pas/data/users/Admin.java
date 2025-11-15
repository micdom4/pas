package team.four.pas.data.users;

import java.util.UUID;

public class Admin extends User{
    public Admin(UUID id, String login, String name, String surname) {
        super(id, login, name, surname);
    }
}
