package team.four.pas.repositories;
import team.four.pas.data.*;

public interface OrderRepository {

    Order getOrder(long id);
    boolean createOrder(Order order);
    boolean updateOrder(long id);
}
