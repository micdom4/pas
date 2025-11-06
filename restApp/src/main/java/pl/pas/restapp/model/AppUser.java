package pl.pas.restapp.model;

import lombok.Getter;
import org.springframework.data.annotation.Id;

@Getter
public abstract class AppUser {

    @Id
    private long id;
    private boolean active = false;
    private final String login;

    public AppUser(String login) {
        this.login = login;
    }

    public void activate() {
        this.active = true;
    }

    public void deactivate() {
        this.active = false;
    }
}
