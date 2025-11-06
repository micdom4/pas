package pl.pas.restapp.repositories;


import pl.pas.restapp.model.OrderActor;
import pl.pas.restapp.model.Restaurant;

public sealed interface OrderActorRepository permits OrderActorRepositoryImpl {

    <T extends OrderActor> boolean save(T client);

    <T extends OrderActor> T findById(long id, Class<T> clazz);

    public Restaurant update(long restaurant_id, String new_office_address);

    <T extends OrderActor> T delete(long id, Class<T> clazz);
}
