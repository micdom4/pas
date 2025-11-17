package team.four.pas.services.implementation;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import team.four.pas.repositories.UserRepository;
import team.four.pas.services.UserService;
import team.four.pas.services.data.users.User;

import java.util.List;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    @NonNull
    private final UserRepository userRepository;

    @Override
    public List<User> getAll() {
        return userRepository.getAll();
    }

    @Override
    public User findById(String id) {
        return userRepository.findById(id);
    }

    @Override
    public User findByLogin(String login) {
        return userRepository.findByLogin(login);
    }

    @Override
    public List<User> findByMatchingLogin(String login) {
        return userRepository.findByMatchingLogin(login);
    }

    @Override
    public <T extends User> boolean add(String login, String name, String surname, Class<T> userClass) {
        if (validateLogin(login) && validateName(name) && validateSurname(surname)) {
            return userRepository.add(login, name, surname, userClass);
        } else {
            return false;
        }
    }

    @Override
    public boolean update(String id, String surname) {
        if (validateSurname(surname)) {
            return userRepository.update(id, surname);
        } else {
            return false;
        }
    }

    @Override
    public boolean updateByLogin(String login, String surname) {
        if (validateLogin(login) && validateSurname(surname)) {
            return userRepository.updateByLogin(login, surname);
        } else {
            return false;
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
