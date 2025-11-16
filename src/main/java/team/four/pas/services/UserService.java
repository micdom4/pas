package team.four.pas.services;

import team.four.pas.services.data.users.User;

import java.util.List;

public interface UserService {
    List<User> getAll();

    User findById(String id);

    User findByLogin(String login);

    List<User> findByMatchingLogin(String login);

    <T extends User> boolean add(String login, String name, String surname, Class<T> userClass);

    boolean update(String id, String surname);

    boolean updateByLogin(String login, String surname);

    boolean activate(String id);

    boolean deactivate(String id);
}
