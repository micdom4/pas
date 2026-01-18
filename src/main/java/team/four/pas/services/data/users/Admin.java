package team.four.pas.services.data.users;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import team.four.pas.security.SecurityRoles;

import java.util.Collection;
import java.util.List;

public class Admin extends User {

    public Admin(String id, String login,
                 String password, String name, String surname,
                 boolean active) {
        super(id, login, password, name, surname, active);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(SecurityRoles.ADMIN));
    }
}
