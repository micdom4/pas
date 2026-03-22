package team.four.pas.services.implementation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import team.four.pas.exceptions.user.*;
import team.four.pas.outside.UserWebPort;
import team.four.pas.data.users.User;
import team.four.pas.inside.UserPersistencePort;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserWebPort {
    private final UserPersistencePort userPort;

    @Override
    public List<User> getAll() {
        return userPort.findAll();
    }

    @Override
    public User findById(String id) throws UserNotFoundException, UserIdException {
        return userPort.findById(id).orElseThrow(() -> new UserNotFoundException("not found"));
    }

    @Override
    public User findByLogin(String login) throws UserNotFoundException, UserLoginException {
        return userPort.findByLogin(login);
    }

    @Override
    public List<User> findByMatchingLogin(String login) {
        return userPort.findByLoginStartingWith(login);
    }

    @Override
    public User add(User user) throws UserDataException, UserTypeException, UserAlreadyExistsException, UserLoginException {
        return userPort.insert(user);
    }

    @Override
    public void update(String id, String surname) throws UserDataException, UserNotFoundException, UserIdException {
        userPort.updateSurnameById(id, surname);
    }

    @Override
    public void activate(String id) throws UserNotFoundException, UserIdException {
        userPort.updateActiveById(id, true);
    }

    @Override
    public void deactivate(String id) throws UserNotFoundException, UserIdException {
        userPort.updateActiveById(id, false);
    }
}
