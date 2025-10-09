package team.four.nbd.data;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "food_order")
public class FoodOrder extends Order {
    private int restaurantId;
    private int workerId;
    private String restaurantAddress;
}
