package team.four.pas.services.implementation;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import team.four.pas.controllers.DTOs.UserAddDTO;
import team.four.pas.controllers.DTOs.UserDTO;
import team.four.pas.exceptions.user.*;
import team.four.pas.repositories.UserRepository;
import team.four.pas.services.UserService;
import team.four.pas.services.data.users.Admin;
import team.four.pas.services.data.users.Client;
import team.four.pas.services.data.users.Manager;
import team.four.pas.services.mappers.UserToDTO;

import java.util.List;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserToDTO userToDTO;

    @Override
    public List<UserDTO> getAll() throws UserGetAllException {
        try {
            return userToDTO.toDataList(userRepository.getAll());
        } catch (Exception e) {
            throw new UserGetAllException(e.getMessage(), e);
        }
    }

    @Override
    public UserDTO findById(String id) throws UserFindException {
        try {
            return userToDTO.toDTO(userRepository.findById(id));
        } catch (Exception e) {
            throw new UserFindException(e.getMessage(), e);
        }
    }

    @Override
    public UserDTO findByLogin(String login) throws UserFindException {
        try {
            return userToDTO.toDTO(userRepository.findByLogin(login));
        } catch (Exception e) {
            throw new UserFindException(e.getMessage(), e);
        }
    }

    @Override
    public List<UserDTO> findByMatchingLogin(String login) throws UserFindException {
        try {
            return userToDTO.toDataList(userRepository.findByMatchingLogin(login));
        } catch (Exception e) {
            throw new UserFindException(e.getMessage(), e);
        }
    }

    @Override
    public UserDTO add(UserAddDTO addDTO) throws UserAddException {
        try {
            validateLogin(addDTO.login());
            validateName(addDTO.name());
            validateSurname(addDTO.name());

            return switch (addDTO.type()) {
                case CLIENT ->
                        userToDTO.toDTO(userRepository.add(addDTO.login(), addDTO.name(), addDTO.surname(), Client.class));
                case MANAGER ->
                        userToDTO.toDTO(userRepository.add(addDTO.login(), addDTO.name(), addDTO.surname(), Manager.class));
                case ADMIN ->
                        userToDTO.toDTO(userRepository.add(addDTO.login(), addDTO.name(), addDTO.surname(), Admin.class));
            };

        } catch (Exception e) {
            throw new UserAddException(e.getMessage(), e);
        }
    }

    @Override
    public UserDTO update(String id, String surname) throws UserUpdateException {
        try {
            validateSurname(surname);

            return userToDTO.toDTO(userRepository.update(id, surname));
        } catch (Exception e) {
            throw new UserUpdateException(e.getMessage(), e);
        }
    }

    @Override
    public void activate(String id) throws UserUpdateException {
        try {
            userRepository.activate(id);
        } catch (Exception e) {
            throw new UserUpdateException(e.getMessage(), e);
        }

    }

    @Override
    public void deactivate(String id) throws UserUpdateException {
        try {
            userRepository.deactivate(id);
        } catch (Exception e) {
            throw new UserUpdateException(e.getMessage(), e);
        }
    }

    private void validateLogin(String login) throws UserDataValidationException {
        final Pattern pattern = Pattern.compile("^[A-Z][A-Z][a-z]{1,18}[0-9]{0,5}$");
        if (!pattern.matcher(login).matches()) {
            throw new UserDataValidationException("Wrong format of login");
        }
    }

    private void validateName(String name) throws UserDataValidationException {
        final Pattern pattern = Pattern.compile("^[A-Z][a-z]{1,19}$");
        if (!pattern.matcher(name).matches()) {
            throw new UserDataValidationException("Wrong format of name");
        }
    }

    private void validateSurname(String surname) throws UserDataValidationException {
        final Pattern pattern = Pattern.compile("^[A-Z][a-z]{1,19}(-[A-Z][a-z]{1,19})?$");
        if (!pattern.matcher(surname).matches()) {
            throw new UserDataValidationException("Wrong format of surname");
        }
    }
}
