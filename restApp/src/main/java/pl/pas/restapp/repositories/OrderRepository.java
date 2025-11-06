package pl.pas.restapp.repositories;

import java.util.List;

public sealed interface OrderRepository<T> permits OrderRepositoryImpl {

    T save(T client);

    T findById(long id);

    List<T> findAll();

    T update(T client);

    boolean delete(long id);
}
