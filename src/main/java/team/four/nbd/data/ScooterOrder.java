package team.four.nbd.data;

import jakarta.annotation.Nullable;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "scooter_order")
public class ScooterOrder extends Order {

    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull
    private Scooter scooter;
    private String startLocation;
}
