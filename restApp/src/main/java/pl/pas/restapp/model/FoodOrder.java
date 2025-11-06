package pl.pas.restapp.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter @Setter
@Document("orders")
@TypeAlias("FoodOrder")
public final class FoodOrder extends Order {

    @Field("foodDetails.restaurantId")
    private long restaurantId;
    @Field("foodDetails.restaurantAddress")
    private String restaurantAddress;
}
