package team.four.nbd.data;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class Restaurant {

    @Id
    private int id;
    private String name;
    private String officeAddress;
}
