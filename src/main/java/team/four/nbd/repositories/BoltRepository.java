package team.four.nbd.repositories;

import team.four.nbd.data.*;

public interface BoltRepository {
    Client getClients(int id);
    Order getOrder(int id);
    Restaurant getRestaurant(int id);
    Scooter getScooter(int id);
}
