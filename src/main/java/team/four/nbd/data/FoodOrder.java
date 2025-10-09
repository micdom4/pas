package team.four.nbd.data;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "food_order")
public class FoodOrder extends Order {

    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull
    private Restaurant restaurant;
    private int workerId;
    private String restaurantAddress;
}
