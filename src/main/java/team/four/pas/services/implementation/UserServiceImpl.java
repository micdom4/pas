package team.four.pas.services.implementation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import team.four.pas.exceptions.user.*;
import team.four.pas.repositories.UserRepository;
import team.four.pas.services.UserService;
import team.four.pas.services.data.users.User;

import java.util.List;
import java.util.regex.Pattern;

@Service
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
        validateLogin(user.getLogin());
        validateName(user.getName());
        validateSurname(user.getSurname());

//        return switch (user.getClass()) {
//            case Client.class ->
//                    userRepository.add(user.getlogin(), user.getname(), user.getsurname(), Client.class);
//            case MANAGER ->
//                    userRepository.add(user.getlogin(), user.getname(), user.getsurname(), Manager.class);
//            case ADMIN ->
//                    userRepository.add(user.getlogin(), user.getname(), user.getsurname(), Admin.class);
//        };

        return userRepository.add(user.getLogin(), user.getName(), user.getSurname(), user.getClass());
    }

    @Override
    public User update(String id, String surname) throws UserDataException, UserNotFoundException, UserIdException {
        validateSurname(surname);

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

    private void validateLogin(String login) throws UserLoginException {
        final Pattern pattern = Pattern.compile("^[A-Z][A-Z][a-z]{1,18}[0-9]{0,5}$");
        if (!pattern.matcher(login).matches()) {
            throw new UserLoginException("Wrong format of login");
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
