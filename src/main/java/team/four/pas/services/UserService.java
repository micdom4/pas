package team.four.pas.services;

import team.four.pas.controllers.DTOs.UserAddDTO;
import team.four.pas.controllers.DTOs.UserDTO;
import team.four.pas.exceptions.user.UserAddException;
import team.four.pas.exceptions.user.UserFindException;
import team.four.pas.exceptions.user.UserGetAllException;
import team.four.pas.exceptions.user.UserUpdateException;

import java.util.List;

public interface UserService {
    List<UserDTO> getAll() throws UserGetAllException;

    UserDTO findById(String id) throws UserFindException;

    UserDTO findByLogin(String login) throws UserFindException;

    List<UserDTO> findByMatchingLogin(String login) throws UserFindException;

    UserDTO add(UserAddDTO addDTO) throws UserAddException;

    UserDTO update(String id, String surname) throws UserUpdateException;

    void activate(String id) throws UserUpdateException;

    void deactivate(String id) throws UserUpdateException;
}
