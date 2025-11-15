package team.four.pas.data;

import lombok.Data;

@Data
public class FoodOrder extends Order {

    private Restaurant restaurant;
    private String restaurantAddress;
}
