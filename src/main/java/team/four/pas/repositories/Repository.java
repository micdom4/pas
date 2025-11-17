package team.four.pas.repositories;
import java.util.List;

interface Repository<T> {
    List<T> getAll();

    T findById(String id);
}
