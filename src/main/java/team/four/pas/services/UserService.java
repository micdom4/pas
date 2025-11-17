package team.four.pas.services;

import team.four.pas.controllers.DTOs.UserAddDTO;
import team.four.pas.controllers.DTOs.UserDTO;
import team.four.pas.services.data.users.User;

import javax.management.BadAttributeValueExpException;
import java.rmi.ServerException;
import java.security.KeyManagementException;
import java.util.List;

public interface UserService {
    List<UserDTO> getAll();

    UserDTO findById(String id);

    UserDTO findByLogin(String login);

    List<UserDTO> findByMatchingLogin(String login);

    UserDTO add(UserAddDTO addDTO) throws ServerException, KeyManagementException, BadAttributeValueExpException;

    UserDTO update(String id, String surname);

    boolean activate(String id);

    boolean deactivate(String id);
}
