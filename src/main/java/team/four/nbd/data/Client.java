package team.four.nbd.data;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import jakarta.persistence.Id;

import java.util.List;

@Entity
@Table(name = "CLIENTS")
public class Client {

    public Client() {
    }

    public Client(long id, String name, String surname) {
        this.id = id;
        this.name = name;
        Surname = surname;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private String Surname;
}
