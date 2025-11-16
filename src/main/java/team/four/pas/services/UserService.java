package team.four.pas.services;

import team.four.pas.services.data.users.User;

import java.util.List;

public interface UserService {
    List<User> getAll();

    User findById(String id);

    User findByLogin(String login);

    boolean activate(String id);

    boolean deactivate(String id);
}
