package team.four.pas.repositories;

import team.four.pas.services.data.users.User;

import javax.management.BadAttributeValueExpException;
import java.rmi.ServerException;
import java.security.KeyManagementException;
import java.util.List;

public interface UserRepository  extends Repository<User> {
    User findByLogin(String login);

    List<User> findByLogin(List<String> login);

    List<User> findByMatchingLogin(String loginStart);

    <T extends User> User add(String login, String name, String surname, Class<T> userClass) throws ServerException, KeyManagementException, BadAttributeValueExpException;

    User update(String id, String Surname);

    boolean activate(String id);

    boolean deactivate(String id);
}
