package team.four.nbd.data;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Scooter {

    @Id
    private long id;
    private boolean working;
    private String currentLocation;
}
