package team.four.nbd.repositories;


import team.four.nbd.data.Client;
import team.four.nbd.data.Order;

import java.util.Set;

public interface ClientRepository {

    Client getClient(long id);
    Set<Order> getClientOrders(long client_id);
}
