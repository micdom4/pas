package team.four.pas.services.data.users;

import lombok.Getter;

@Getter
public class Manager extends User {
    public Manager(String id, String login, String name, String surname, boolean active) {
        super(id, login, name, surname, active);
    }
}
