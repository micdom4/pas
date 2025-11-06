package pl.pas.restapp.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Document("restaurants")
@NoArgsConstructor
public final class Restaurant implements OrderActor {

    @Id
    private long id;
    private String name;
    private String officeAddress;

    @Version
    private long version;

    public Restaurant(long id, String name, String officeAddress, long version) {
        this.id = id;
        this.name = name;
        this.officeAddress = officeAddress;
        this.version = version;
    }
}
