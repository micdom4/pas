package team.four.pas.repositories;

import team.four.pas.data.users.User;

import java.util.*;
import java.util.stream.Collectors;

public class LocalUserRepository implements UserRepository {

    private final UserMap idLoginMap = new UserMap();

    @Override
    public User findByLogin(String login) {
        return idLoginMap.get(login);
    }

    public List<User> findByMatchingLogin(String loginStart) {
        List<String> matchingLogins = idLoginMap.getLogins()
                                                .stream()
                                                .filter(log -> log.startsWith(loginStart))
                                                .collect(Collectors.toList());
        return idLoginMap.get(matchingLogins);
    }

    @Override
    public User findById(UUID id) {
        return idLoginMap.get(id);
    }

    public boolean add(String login, String name, String surname) {
        if(idLoginMap.contains(login)){

        }
        //Check if login is unique, Create a new unique UUID
        idLoginMap.put(user.getLogin(), user);
        return true;
    }


    @Override
    public boolean update(User user) {
        return false;
    }

    @Override
    public boolean delete(UUID id) {
        // Check if he has active allocations, and exists
        idLoginMap.remove(id);
        return true;
    }


    private class UserMap {
        Map<UUID, User> idToUser = new HashMap<>();
        Map<String, User> loginToUser  = new HashMap<>();

        User get(UUID id) {
            return idToUser.get(id);
        }

        User get(String login) {
            return loginToUser.get(login);
        }

        List<User> get(List<String> logins) {
            return logins.stream()
                         .map(login -> loginToUser.get(login))
                         .collect(Collectors.toList());
        }

        Set<String> getLogins() {
            return loginToUser.keySet();
        }

        boolean contains(String login) {
            return loginToUser.containsKey(login);
        }

        void put(String login, User user) {
            loginToUser.put(login, user);
            idToUser.put(user.getId(), user);
        }

        void remove(UUID id) {
            loginToUser.remove(idToUser.get(id));
            idToUser.remove(id);
        }

        void remove(String login) {
            idToUser.remove(loginToUser.get(login));
            loginToUser.remove(login);
        }
    }
}
