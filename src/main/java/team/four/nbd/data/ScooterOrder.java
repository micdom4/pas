package team.four.nbd.data;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "scooter_order")
public class ScooterOrder extends Order {
    private int scooterId;
    private String startLocation;
}
