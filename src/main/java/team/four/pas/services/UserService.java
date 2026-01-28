package team.four.pas.services;

import team.four.pas.controllers.DTOs.UserAddDTO;
import team.four.pas.controllers.DTOs.UserDTO;
import team.four.pas.exceptions.user.*;
import team.four.pas.services.data.users.User;

import java.util.List;

public interface UserService {
    List<User> getAll();

    User findById(String id) throws UserNotFoundException, UserIdException;

    User findByLogin(String login) throws UserNotFoundException, UserLoginException;

    List<User> findByMatchingLogin(String login);

    User add(User user) throws UserDataException, UserTypeException, UserAlreadyExistsException, UserLoginException;

    User update(String id, String surname) throws UserDataException, UserNotFoundException, UserIdException;

    void activate(String id) throws UserNotFoundException, UserIdException;

    void deactivate(String id) throws UserNotFoundException, UserIdException;
}
