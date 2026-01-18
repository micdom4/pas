package team.four.pas.services.data.users;


import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import team.four.pas.controllers.DTOs.UserType;

import java.util.Collection;
import java.util.List;

public class Client extends User {
    public Client(String id, String login,
                  String password, String name, String surname,
                  boolean active) {
        super(id, login, password, name, surname, active);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(UserType.CLIENT.toString()));
    }
}
