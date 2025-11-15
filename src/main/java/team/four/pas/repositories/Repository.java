package team.four.pas.repositories;
import java.util.List;
import java.util.UUID;

interface Repository<T> {
    List<T> getAll();

    T findById(UUID id);

   List<T> findById(List<UUID> id);

    boolean delete(UUID id);
}
