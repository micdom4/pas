package team.four.pas.services.implementation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import team.four.pas.exceptions.user.*;
import team.four.pas.repositories.UserRepository;
import team.four.pas.services.UserService;
import team.four.pas.services.data.users.User;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Override
    public User findById(String id) throws UserNotFoundException, UserIdException {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("not found"));
    }

    @Override
    public User findByLogin(String login) throws UserNotFoundException, UserLoginException {
        return userRepository.findByLogin(login);
    }

    @Override
    public List<User> findByMatchingLogin(String login) {
        return userRepository.findByLoginStartingWith(login);
    }

    @Override
    public User add(User user) throws UserDataException, UserTypeException, UserAlreadyExistsException, UserLoginException {
//        return switch (user.getClass()) {
//            case Client.class ->
//                    userRepository.add(user.getlogin(), user.getname(), user.getsurname(), Client.class);
//            case MANAGER ->
//                    userRepository.add(user.getlogin(), user.getname(), user.getsurname(), Manager.class);
//            case ADMIN ->
//                    userRepository.add(user.getlogin(), user.getname(), user.getsurname(), Admin.class);
//        };

        return userRepository.insert(user);
    }

    @Override
    public void update(String id, String surname) throws UserDataException, UserNotFoundException, UserIdException {
        userRepository.updateSurnameById(id, surname);
    }

    @Override
    public void activate(String id) throws UserNotFoundException, UserIdException {
        userRepository.updateActiveById(id, true);
    }

    @Override
    public void deactivate(String id) throws UserNotFoundException, UserIdException {
        userRepository.updateActiveById(id, false);
    }
}
