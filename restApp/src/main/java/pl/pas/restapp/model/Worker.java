package pl.pas.restapp.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter @Setter
@Document("workers")
public final class Worker implements OrderActor {

    @Id
    private long id;
    private String Name;
    private String Surname;
    private String id_card_url;

    @Version
    private long version;
}
