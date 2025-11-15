package team.four.pas.data;

import lombok.Data;

import java.util.Set;

@Data
public class Client {

    private long id;
    private String name;
    private String surname;

    private long version;

    private Set<Order> orders;

    @Override
    public String toString() {
        return  id + " " + name + " " + surname;
    }
}
