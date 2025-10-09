package team.four.nbd.repositories;

import team.four.nbd.data.*;

import java.util.Set;

public interface ZoltRepository {
    Client getClients(long id);
    Order getOrder(long id);
    Set<Order> getClientOrders(long client_id);

    boolean createOrder(Order order);
    boolean updateOrder(Order order);
    boolean deleteOrder(long id);
}
