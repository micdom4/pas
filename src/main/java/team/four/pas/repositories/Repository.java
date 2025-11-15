package team.four.pas.repositories;
import java.util.UUID;

interface Repository<T> {
    T findById(UUID id);

    boolean delete(UUID id);
}
