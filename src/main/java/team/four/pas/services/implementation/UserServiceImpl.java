package team.four.pas.services.implementation;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import team.four.pas.controllers.DTOs.UserAddDTO;
import team.four.pas.controllers.DTOs.UserDTO;
import team.four.pas.repositories.UserRepository;
import team.four.pas.services.UserService;
import team.four.pas.services.data.users.Admin;
import team.four.pas.services.data.users.Client;
import team.four.pas.services.data.users.Manager;
import team.four.pas.services.mappers.UserToDTO;

import javax.management.BadAttributeValueExpException;
import java.rmi.ServerException;
import java.security.KeyManagementException;
import java.util.List;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    @NonNull
    private final UserRepository userRepository;
    @NonNull
    private final UserToDTO userToDTO;

    @Override
    public List<UserDTO> getAll() {
        return userToDTO.toDataList(userRepository.getAll());
    }

    @Override
    public UserDTO findById(String id) {
        return userToDTO.toDTO(userRepository.findById(id));
    }

    @Override
    public UserDTO findByLogin(String login) {
        return userToDTO.toDTO(userRepository.findByLogin(login));
    }

    @Override
    public List<UserDTO> findByMatchingLogin(String login) {
        return userToDTO.toDataList(userRepository.findByMatchingLogin(login));
    }

    @Override
    public UserDTO add(UserAddDTO addDTO) throws ServerException, KeyManagementException, BadAttributeValueExpException {
        if (validateLogin(addDTO.login()) && validateName(addDTO.name()) && validateSurname(addDTO.name())) {
            return switch (addDTO.type()) {
                case CLIENT -> userToDTO.toDTO(userRepository.add(addDTO.login(), addDTO.name(), addDTO.surname(), Client.class));
                case MANAGER -> userToDTO.toDTO(userRepository.add(addDTO.login(), addDTO.name(), addDTO.surname(), Manager.class));
                case ADMIN-> userToDTO.toDTO(userRepository.add(addDTO.login(), addDTO.name(), addDTO.surname(), Admin.class));
                default -> throw new IllegalArgumentException("Unknown user type: " + addDTO.type());
            };
        } else {
            throw new BadAttributeValueExpException("Data doesn't conform to standards");
        }
    }

    @Override
    public UserDTO update(String id, String surname) {
        if (validateSurname(surname)) {
            return userToDTO.toDTO(userRepository.update(id, surname));
        } else {
            return null;
        }
    }

    @Override
    public boolean activate(String id) {
        return userRepository.activate(id);
    }

    @Override
    public boolean deactivate(String id) {
        return userRepository.deactivate(id);
    }

    private boolean validateLogin(String login) {
        final Pattern pattern = Pattern.compile("^[A-Z][A-Z][a-z]{1,18}[0-9]{0,5}$");
        return pattern.matcher(login).matches();
    }

    private boolean validateName(String name) {
        final Pattern pattern = Pattern.compile("^[A-Z][a-z]{1,19}$");
        return pattern.matcher(name).matches();
    }

    private boolean validateSurname(String surname) {
        final Pattern pattern = Pattern.compile("^[A-Z][a-z]{1,19}(-[A-Z][a-z]{1,19})?$");
        return pattern.matcher(surname).matches();
    }
}
