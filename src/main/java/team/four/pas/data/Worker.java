package team.four.pas.data;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.Data;

@Data
@Entity
@Table(name = "worker")
public class Worker {

    @Id
    private long id;
    private String Name;
    private String Surname;
    private byte[] id_card;

    @Version
    private long version;
}
