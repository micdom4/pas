package team.four.pas.repositories;


import java.util.UUID;

interface Repository<T> {
    boolean add(T t);

    T findById(UUID id);

    boolean update(T t);

    boolean delete(UUID id);
}
