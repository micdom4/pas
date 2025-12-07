package team.four.pas.services.implementation;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import team.four.pas.exceptions.user.*;
import team.four.pas.repositories.UserRepository;
import team.four.pas.services.UserService;
import team.four.pas.services.data.users.User;

import java.util.List;

@ApplicationScoped
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public List<User> getAll() {
        return userRepository.getAll();
    }

    @Override
    public User findById(String id) throws UserNotFoundException, UserIdException {
        return userRepository.findById(id);
    }

    @Override
    public User findByLogin(String login) throws UserNotFoundException, UserLoginException {
        return userRepository.findByLogin(login);
    }

    @Override
    public List<User> findByMatchingLogin(String login) {
        return userRepository.findByMatchingLogin(login);
    }

    @Override
    public User add(User user) throws UserDataException, UserTypeException, UserAlreadyExistsException, UserLoginException {
        return userRepository.add(user.getLogin(), user.getName(), user.getSurname(), user.getClass());
    }

    @Override
    public User update(String id, String surname) throws UserDataException, UserNotFoundException, UserIdException {
        return userRepository.update(id, surname);
    }

    @Override
    public void activate(String id) throws UserNotFoundException, UserIdException {
        userRepository.activate(id);
    }

    @Override
    public void deactivate(String id) throws UserNotFoundException, UserIdException {
        userRepository.deactivate(id);
    }
}