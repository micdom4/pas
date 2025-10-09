package team.four.nbd.data;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "clients")
public class Client {

    public Client() {
    }

    public Client(long id, String name, String surname) {
        this.id = id;
        this.name = name;
        this.surname = surname;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private String surname;

    @OneToMany(mappedBy = "client")
    private Set<Order> orders;

    @Override
    public String toString() {
        return  id + " " + name + " " + surname;
    }
}
