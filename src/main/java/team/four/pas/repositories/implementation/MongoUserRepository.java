package team.four.pas.repositories.implementation;

import com.mongodb.client.MongoCollection;
import team.four.pas.repositories.UserRepository;
import team.four.pas.repositories.entities.UserEntity;
import team.four.pas.repositories.mappers.UserMapper;
import team.four.pas.services.data.users.User;

import java.util.List;

public class MongoUserRepository implements UserRepository {
    private final MongoCollection<UserEntity> userCollection;
    private final UserMapper mapper;

    public MongoUserRepository(MongoCollection<UserEntity> userCollection, UserMapper mapper) {
        this.userCollection = userCollection;
        this.mapper = mapper;
    }

    @Override
    public User findByLogin(String login) {
        return null;
    }

    @Override
    public List<User> findByLogin(List<String> login) {
        return List.of();
    }

    @Override
    public List<User> findByMatchingLogin(String loginStart) {
        return List.of();
    }

    @Override
    public <T extends User> boolean add(String login, String name, String surname, Class<T> userClass) {
        return false;
    }

    @Override
    public boolean updateByLogin(String id, String Surname) {
        return false;
    }

    @Override
    public boolean update(String login, String Surname) {
        return false;
    }

    @Override
    public List<User> getAll() {
        return List.of();
    }

    @Override
    public User findById(String id) {
        return null;
    }

    @Override
    public List<User> findById(List<String> id) {
        return List.of();
    }

    @Override
    public boolean delete(String id) {
        return false;
    }
}
