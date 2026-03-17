package team.four.pas.adapters;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import team.four.pas.mappers.UserMapper;
import team.four.pas.repositories.UserRepository;
import team.four.pas.services.data.users.User;
import team.four.pas.inside.UserPersistencePort;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserAdapter implements UserPersistencePort {
   private final UserRepository repository;
   private final UserMapper mapper;

    @Override
    public List<User> findAll() {
        return mapper.toDomain(repository.findAll());
    }

    @Override
    public Optional<User> findById(String id) {
        return Optional.ofNullable(
                mapper.toDomain(
                    repository.findById(id).orElse(null)
                )
        );
    }

    @Override
    public User findByLogin(String login) {
        return mapper.toDomain(repository.findByLogin(login));
    }

    @Override
    public List<User> findByLoginStartingWith(String login) {
        return mapper.toDomain(repository.findByLoginStartingWith(login));
    }

    @Override
    public User insert(User user) {
        return mapper.toDomain(repository.insert(mapper.toEntity(user)));
    }

    @Override
    public void updateSurnameById(String id, String surname) {
        repository.updateSurnameById(id, surname);
    }

    @Override
    public void updateActiveById(String id, boolean b) {
        repository.updateActiveById(id, b);
    }

    @Override
    public void updatePasswordById(String id, String encode) {
        repository.updatePasswordById(id, encode);
    }
}
