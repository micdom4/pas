package team.four.nbd.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import team.four.nbd.data.Client;
import team.four.nbd.data.Order;
import team.four.nbd.data.Restaurant;
import team.four.nbd.data.Scooter;


@Repository
public class BoltRepositoryImpl implements BoltRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Client getClients(int id) {
        return em.find(Client.class, 1L);
    }

    @Override
    public Order getOrder(int id) {
        return null;
    }

    @Override
    public Restaurant getRestaurant(int id) {
        return null;
    }

    @Override
    public Scooter getScooter(int id) {
        return null;
    }
}
