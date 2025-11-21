package team.four.pas.services.implementation;

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
    public List<UserDTO> getAll() {
        return userToDTO.toDataList(userRepository.getAll());
    }

    @Override
    public UserDTO findById(String id) throws UserNotFoundException, UserIdException {
        return userToDTO.toDTO(userRepository.findById(id));
    }

    @Override
    public UserDTO findByLogin(String login) throws UserNotFoundException, UserLoginException {
        return userToDTO.toDTO(userRepository.findByLogin(login));
    }

    @Override
    public List<UserDTO> findByMatchingLogin(String login) {
        return userToDTO.toDataList(userRepository.findByMatchingLogin(login));
    }

    @Override
    public UserDTO add(UserAddDTO addDTO) throws UserDataException, UserTypeException, UserAlreadyExistsException, UserLoginException {
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
    }

    @Override
    public UserDTO update(String id, String surname) throws UserDataException, UserNotFoundException, UserIdException {
        validateSurname(surname);

        return userToDTO.toDTO(userRepository.update(id, surname));
    }

    @Override
    public void activate(String id) throws UserNotFoundException, UserIdException {
        userRepository.activate(id);
    }

    @Override
    public void deactivate(String id) throws UserNotFoundException, UserIdException {
        userRepository.deactivate(id);
    }

    private void validateLogin(String login) throws UserDataException {
        final Pattern pattern = Pattern.compile("^[A-Z][A-Z][a-z]{1,18}[0-9]{0,5}$");
        if (!pattern.matcher(login).matches()) {
            throw new UserDataException("Wrong format of login");
        }
    }

    private void validateName(String name) throws UserDataException {
        final Pattern pattern = Pattern.compile("^[A-Z][a-z]{1,19}$");
        if (!pattern.matcher(name).matches()) {
            throw new UserDataException("Wrong format of name");
        }
    }

    private void validateSurname(String surname) throws UserDataException {
        final Pattern pattern = Pattern.compile("^[A-Z][a-z]{1,19}(-[A-Z][a-z]{1,19})?$");
        if (!pattern.matcher(surname).matches()) {
            throw new UserDataException("Wrong format of surname");
        }
    }
}
