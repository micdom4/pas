package team.four.pas.repositories;

import team.four.pas.data.users.User;

import java.util.List;
import java.util.UUID;

public interface UserRepository  extends Repository<User> {
    User findByLogin(String login);

    List<User> findByMatchingLogin(String loginStart);

    public <T extends User> boolean add(String login, String name, String surname, Class<T> userClass);

    public boolean update(UUID id, String Surname);

    public boolean update(String login, String Surname);

    public boolean delete(String login);
}
