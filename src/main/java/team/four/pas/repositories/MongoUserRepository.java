package team.four.pas.repositories;

import com.mongodb.client.MongoCollection;
import team.four.pas.data.users.User;

import java.util.List;
import java.util.UUID;

public class MongoUserRepository implements UserRepository {
    public final MongoCollection<User> userCollection;

    public MongoUserRepository(MongoCollection<User> userCollection) {
        this.userCollection = userCollection;
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
    public boolean update(UUID id, String Surname) {
        return false;
    }

    @Override
    public boolean update(String login, String Surname) {
        return false;
    }

    @Override
    public boolean delete(String login) {
        return false;
    }

    @Override
    public List<User> getAll() {
        return List.of();
    }

    @Override
    public User findById(UUID id) {
        return null;
    }

    @Override
    public List<User> findById(List<UUID> id) {
        return List.of();
    }

    @Override
    public boolean delete(UUID id) {
        return false;
    }
}
