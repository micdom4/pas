package team.four.nbd.data;

import jakarta.persistence.*;

import java.util.Set;

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

    public long getId() {
        return id;
    }

    @OneToMany(mappedBy = "client")
    private Set<Order> orders;

    public Set<Order> getOrders() {
        return orders;
    }
    @Override
    public String toString() {
        return  id + " " + name + " " + surname;
    }
}
