package team.four.pas.services;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import team.four.pas.repositories.UserRepository;
import team.four.pas.services.data.users.User;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    @NonNull private UserRepository repository;

    public List<User> getAll(){
        return repository.getAll();
    }

    public User findById(String id) {
        return repository.findById(id);
    }

    public User findByLogin(String login) {
        return repository.findByLogin(login);
    }

    public boolean activate(String id) {
        return repository.activate(id);
    }

    public boolean deactivate(String id) {
        return repository.deactivate(id);
    }

    /*
    List<User> findByLogin(List<String> login) {

    }

    List<User> findByMatchingLogin(String loginStart) {

    }

    <T extends User> boolean add(String login, String name, String surname, Class<T> userClass) {

    }

    boolean update(String id, String Surname) {

    }

    boolean updateByLogin(String login, String Surname) {

    }

     */
}
