package team.four.pas.services;

import team.four.pas.controllers.DTOs.UserAddDTO;
import team.four.pas.controllers.DTOs.UserDTO;
import team.four.pas.exceptions.user.*;

import java.util.List;

public interface UserService {
    List<UserDTO> getAll();

    UserDTO findById(String id) throws UserNotFoundException, UserIdException;

    UserDTO findByLogin(String login) throws UserNotFoundException, UserLoginException;

    List<UserDTO> findByMatchingLogin(String login);

    UserDTO add(UserAddDTO addDTO) throws UserDataException, UserTypeException, UserAlreadyExistsException, UserLoginException;

    UserDTO update(String id, String surname) throws UserDataException, UserNotFoundException, UserIdException;

    void activate(String id) throws UserNotFoundException, UserIdException;

    void deactivate(String id) throws UserNotFoundException, UserIdException;
}
