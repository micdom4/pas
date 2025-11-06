package pl.pas.restapp.repositories;

import com.mongodb.client.result.UpdateResult;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.*;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pl.pas.restapp.model.OrderActor;
import pl.pas.restapp.model.Restaurant;


@Repository
public non-sealed class OrderActorRepositoryImpl implements OrderActorRepository {
    private final MongoTemplate mongoTemplate;

    public OrderActorRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    @Transactional
    public <T extends OrderActor> T findById(long id, Class<T> clazz) {
        return mongoTemplate.findById(id, clazz);
    }

    @Override
    @Transactional
    public <T extends OrderActor> boolean save(T actor) {
        return mongoTemplate.save(actor).equals(actor);
    }


    @Override
    @Transactional
    public Restaurant update(long restaurant_id, String new_office_address) {
        Query query = new Query().addCriteria(Criteria.where("_id").is(restaurant_id));
        Update update = new Update();
        update.set("officeAddress", new_office_address);

        UpdateResult result = mongoTemplate.updateFirst(query, update, Restaurant.class);
        return result.getModifiedCount() == 1 ? mongoTemplate.findById(restaurant_id, Restaurant.class) : null;
    }

    @Override
    @Transactional
    public <T extends OrderActor> T delete(long id, Class<T> clazz) {
        Query query = new Query().addCriteria(Criteria.where("_id").is(id));
        return mongoTemplate.findAndRemove(query, clazz);
    }
}
