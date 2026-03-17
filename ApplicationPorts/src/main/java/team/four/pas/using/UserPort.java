package team.four.pas.using;

import team.four.pas.services.data.users.User;

import java.util.List;
import java.util.Optional;

public interface UserPort {
    List<User> findAll();

    Optional<User> findById(String id);

    User findByLogin(String login);

    List<User> findByLoginStartingWith(String login);

    User insert(User user);

    void updateSurnameById(String id, String surname);

    void updateActiveById(String id, boolean b);

    void updatePasswordById(String id, String encode);
}
