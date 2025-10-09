package team.four.nbd.data;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Worker {

    @Id
    private long id;
    private String Name;
    private String Surname;
    private byte[] id_card;
}
