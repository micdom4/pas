package team.four.pas.repositories;

import team.four.pas.data.users.User;

import java.util.List;

public interface UserRepository  extends Repository<User> {
    User findByLogin(String login);

    List<User> findByMatchingLogin(String loginStart);
}
