package pl.pas.restapp.repositories;

import com.mongodb.client.result.UpdateResult;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pl.pas.restapp.model.Order;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public non-sealed class OrderRepositoryImpl implements OrderRepository<Order> {
    private final MongoTemplate mongoTemplate;

    public OrderRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Order save(Order order) {
        Query clientActiveTaxiOrders = new Query().addCriteria(Criteria.where("clientId").is(order.getClientId())
                                                                .and("active").is(true)
                                                                .and("_class").is("TaxiOrder"));
        Query workerActiveOrders = new Query().addCriteria(Criteria.where("workerId").is(order.getWorkerId())
                                                            .and("active").is(true));

        List<Order> activeClientTaxiOrders = mongoTemplate.find(clientActiveTaxiOrders, Order.class);
        List<Order> activeWorkerOrders = mongoTemplate.find(workerActiveOrders, Order.class);

        if(!activeClientTaxiOrders.isEmpty() || !activeWorkerOrders.isEmpty()) {
            return null;
        }

        return mongoTemplate.save(order);
    }

    @Override
    public Order findById(long id) {
        return mongoTemplate.findById(id, Order.class);
    }

    @Override
    public List<Order> findAll() {
        return mongoTemplate.findAll(Order.class);
    }

    @Override
    @Transactional
    public Order update(Order order) {
        Query query = new Query().addCriteria(Criteria.where("_id").is(order.getId()));
        Update update = new Update();

        update.set("endTime", LocalDateTime.now());
        update.set("active", false);

        UpdateResult result = mongoTemplate.updateFirst(query, update, Order.class);
        return result.getModifiedCount() == 1 ? mongoTemplate.findById(order.getId(), Order.class) : null;
    }

    @Override
    public boolean delete(long id) {
        Order orderToDelete = mongoTemplate.findById(id, Order.class);

        // 2. IMPORTANT: Check if the order actually exists
        if (orderToDelete == null) {
            // This is what's happening. Log it for proof.
            System.err.println("Order with id " + id + " not found. Cannot delete.");
            return false; // Order doesn't exist, so "delete" was not successful.
        }


        // 4. It exists and is active, so remove it.
        // Pass the object we already found. DO NOT find it again.
        return mongoTemplate.remove(orderToDelete).wasAcknowledged();
    }
}
