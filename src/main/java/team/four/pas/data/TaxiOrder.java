package team.four.pas.data;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "taxi_order")
public class TaxiOrder extends Order {

    public enum Type {
        NORMAL,
        COMFORT,
        PREMIUM,
        WOMAN;
    }

    @Enumerated(EnumType.STRING)
    private Type type;
    private String startLocation;
    private String licensePlate;
}
