package team.four.pas.repositories;

import team.four.pas.data.users.User;

import java.util.List;
import java.util.UUID;

public interface UserRepository  extends Repository<User> {
    User findByLogin(String login);

    List<User> findByLogin(List<String> login);

    List<User> findByMatchingLogin(String loginStart);

    <T extends User> boolean add(String login, String name, String surname, Class<T> userClass);

    boolean update(UUID id, String Surname);

    boolean update(String login, String Surname);

    boolean delete(String login);
}
