package team.four.nbd.repositories;

import team.four.nbd.data.*;

import java.util.Set;

public interface OrderRepository {
    Order getOrder(long id);

    boolean createOrder(Order order);
    boolean updateOrder(Order order);
}
