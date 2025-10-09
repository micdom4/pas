package team.four.nbd.data;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;

@Entity
@Table(name = "taxi_order")
public class TaxiOrder extends Order {
    public enum Type {
        NORMAL,
        COMFORT,
        PREMIUM,
        WOMAN;
    }

    private int workerId;

    @Enumerated(EnumType.STRING)
    private Type type;
    private String startLocation;
    private String licensePlate;
}
