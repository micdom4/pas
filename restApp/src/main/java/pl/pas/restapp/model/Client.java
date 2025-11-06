package pl.pas.restapp.model;

import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Document("clients")
public final class Client extends AppUser implements OrderActor {

    @Id
    private long id;
    private String name;
    private String surname;

    @Version
    private long version;

    public Client(long id, String name, String surname) {
        super("");
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.version = 0;
    }

    public Client(String login) {
        super(login);
    }

    public Client() {
        super("");
    }

    @Override
    public String toString() {
        return  id + " " + name + " " + surname;
    }


}
