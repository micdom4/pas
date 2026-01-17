package team.four.pas.services.data.users;


import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.List;

public class Manager extends User {
    public Manager(String id, String login,
                   String password, String name,
                   String surname, boolean active) {
        super(id, login, password, name, surname, active);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

}
