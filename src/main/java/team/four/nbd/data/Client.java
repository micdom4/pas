package team.four.nbd.data;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;

@Data
@Entity
@Table(name = "clients")
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private String surname;

    @Version
    private long version;

    @OneToMany(mappedBy = "client")
    private Set<Order> orders;

    @Override
    public String toString() {
        return  id + " " + name + " " + surname;
    }
}
