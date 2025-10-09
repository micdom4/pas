package team.four.nbd.data;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "taxi_order")
public class TaxiOrder extends Order {
    public enum Type {
        NORMAL,
        COMFORT,
        PREMIUM,
        WOMAN;
    }

    @ManyToOne
    @NotNull
    private Worker worker;

    @Enumerated(EnumType.STRING)
    private Type type;
    private String startLocation;
    private String licensePlate;
}
