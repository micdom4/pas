package team.four.pas.repositories;

import team.four.pas.exceptions.user.*;
import team.four.pas.services.data.users.User;

import javax.management.BadAttributeValueExpException;
import java.rmi.ServerException;
import java.security.KeyManagementException;
import java.util.List;

public interface UserRepository  extends Repository<User> {
    User findByLogin(String login) throws UserInvalidLoginException, UserNotPresentException;

    List<User> findByMatchingLogin(String loginStart);

    <T extends User> User add(String login, String name, String surname, Class<T> userClass) throws UserInvalidTypeException, UserInvalidLoginException, UserNotPresentException, UserAlreadyExistsException;

    User update(String id, String Surname) throws UserNotPresentException, UserInvalidLoginException, UserInvalidIdException;

    void activate(String id) throws UserInvalidLoginException, UserNotPresentException;

    void deactivate(String id) throws UserInvalidLoginException, UserNotPresentException;
}
