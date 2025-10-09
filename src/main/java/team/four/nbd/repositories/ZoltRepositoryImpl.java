package team.four.nbd.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transaction;
import org.hibernate.HibernateException;
import org.hibernate.annotations.OptimisticLock;
import team.four.nbd.data.Client;
import team.four.nbd.data.Order;

import java.util.Set;


public class ZoltRepositoryImpl implements ZoltRepository {

    public ZoltRepositoryImpl(EntityManager entityManager) {
        this.em = entityManager;
    }

    @PersistenceContext
    private EntityManager em;

    @Override
    public Client getClients(long id) {
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
