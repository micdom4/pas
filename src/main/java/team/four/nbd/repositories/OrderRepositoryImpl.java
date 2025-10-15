package team.four.nbd.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.LockModeType;
import team.four.nbd.data.*;

public class OrderRepositoryImpl implements OrderRepository {

    private final EntityManager em;

    public OrderRepositoryImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public Order getOrder(long id) {
        return em.find(Order.class, id);
    }


    @Override
    public boolean createOrder(Order order) {
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            Worker worker = em.find(Worker.class, order.getWorkerId(), LockModeType.PESSIMISTIC_WRITE);

            if (worker == null) {
                throw new IllegalArgumentException("Worker with ID " + order.getWorkerId() + " not found.");
            }

            Long workerOrderCount = em.createQuery(
                            "SELECT COUNT(o) FROM Order o WHERE o.worker = :worker AND o.active = true", Long.class)
                    .setParameter("worker", worker)
                    .getSingleResult();

            if (workerOrderCount > 0) {
                throw new Exception("Worker " + worker.getId() + " already has an active order.");
            }

            if (order instanceof TaxiOrder) {
                TaxiOrder taxiOrder = (TaxiOrder) order;

                Client client = em.find(Client.class, taxiOrder.getClientId(), LockModeType.PESSIMISTIC_WRITE);

                if (client == null) {
                    throw new IllegalArgumentException("Client with ID " + taxiOrder.getClientId() + " not found.");
                }

                Long clientOrderCount = em.createQuery(
                                "SELECT COUNT(o) FROM TaxiOrder o WHERE o.client = :client AND o.active = true", Long.class)
                        .setParameter("client", client)
                        .getSingleResult();

                if (clientOrderCount > 0) {
                    throw new Exception("Client " + client.getId() + " already has an active taxi order.");
                }
            }

            em.persist(order);

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            return false;
        }

        return true;
    }

    @Override
    public boolean updateOrder(long id) {

        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            Order order = em.find(Order.class, id, LockModeType.PESSIMISTIC_WRITE);

            if (order == null) {
                throw new IllegalArgumentException("order with ID " + id + " not found.");
            }

            order.finishOrder();

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            return false;
        }

        return true;
    }

}
