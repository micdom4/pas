package team.four.nbd.repositories;
import jakarta.persistence.EntityTransaction;
import team.four.nbd.data.*;

public interface OrderRepository {

    Order getOrder(long id);
    boolean createOrder(Order order);
    boolean updateOrder(long id);
}
