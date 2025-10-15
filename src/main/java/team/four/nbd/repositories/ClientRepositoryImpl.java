package team.four.nbd.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.LockModeType;
import team.four.nbd.data.Client;
import team.four.nbd.data.Order;

import java.util.Set;

public class ClientRepositoryImpl implements ClientRepository {
    private final EntityManager em;

    public ClientRepositoryImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public Client getClient(long id) {
        EntityTransaction tx = em.getTransaction();

        Client client = null;

        try {
            tx.begin();
            client = em.find(Client.class, id, LockModeType.OPTIMISTIC);
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        }

        return client;
    }

    @Override
    public Set<Order> getClientOrders(long client_id) {

        EntityTransaction tx = em.getTransaction();
        Set<Order> orders = null;

        try {
            orders = em.find(Client.class, client_id, LockModeType.OPTIMISTIC).getOrders();
        } catch (Exception e) {
            tx.rollback();
        }

        return orders;
    }

}
