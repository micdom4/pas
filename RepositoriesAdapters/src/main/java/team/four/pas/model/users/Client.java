package team.four.pas.model.users;


import org.springframework.data.annotation.TypeAlias;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import team.four.pas.model.SecurityRoles;

import java.util.Collection;
import java.util.List;

@TypeAlias("CLIENT")
public class Client extends User {
    public Client(String id, String login,
                  String password, String name, String surname,
                  boolean active) {
        super(id, login, password, name, surname, active);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(SecurityRoles.CLIENT));
    }
}
