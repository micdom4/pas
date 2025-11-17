package team.four.pas.services;

import team.four.pas.controllers.DTOs.UserAddDTO;
import team.four.pas.controllers.DTOs.UserDTO;
import team.four.pas.services.data.users.User;

import java.util.List;

public interface UserService {
    List<User> getAll();

    User findById(String id);

    User findByLogin(String login);

    List<User> findByMatchingLogin(String login);

    UserDTO add(UserAddDTO addDTO);

    boolean update(String id, String surname);

    boolean updateByLogin(String login, String surname);

    boolean activate(String id);

    boolean deactivate(String id);
}
