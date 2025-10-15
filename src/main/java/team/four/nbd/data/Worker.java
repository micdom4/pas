package team.four.nbd.data;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "worker")
public class Worker {

    @Id
    private long id;
    private String Name;
    private String Surname;
    private byte[] id_card;

    public long getId() {
        return id;
    }
}
