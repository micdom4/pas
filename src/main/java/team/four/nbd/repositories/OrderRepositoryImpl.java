package team.four.nbd.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import team.four.nbd.data.Order;

public class OrderRepositoryImpl implements OrderRepository {

    private final EntityManager em;

    public OrderRepositoryImpl(EntityManager em) {
        this.em = em;
    }

    public Order getOrder(long id) {
        EntityTransaction tx = em.getTransaction();

        Order order = null;
        try {
            tx.begin();
            order = em.find(Order.class, id);
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        }

        return order;
    }

    @Override
    public boolean createOrder(Order order) {
        return false;
    }

    @Override
    public boolean updateOrder(Order order) {
        return false;
    }

    @Override
    public boolean deleteOrder(long id) {
        return false;
    }
}
