package team.four.pas.repositories;

import team.four.pas.exceptions.user.*;
import team.four.pas.services.data.users.User;

import java.util.List;

public interface UserRepository  extends Repository<User> {

    @Override
    User findById(String id) throws UserNotFoundException, UserIdException;

    User findByLogin(String login) throws UserLoginException, UserNotFoundException;

    List<User> findByMatchingLogin(String loginStart);

    <T extends User> User add(String login, String password, String name, String surname, Class<T> userClass) throws UserTypeException, UserLoginException, UserAlreadyExistsException;

    User update(String id, String Surname) throws UserNotFoundException, UserIdException;

    void activate(String id) throws UserNotFoundException, UserIdException;

    void deactivate(String id) throws UserNotFoundException, UserIdException;
}
