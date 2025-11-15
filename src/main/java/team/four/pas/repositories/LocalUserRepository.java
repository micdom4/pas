package team.four.pas.repositories;

import team.four.pas.data.allocations.Allocation;
import team.four.pas.data.users.Admin;
import team.four.pas.data.users.Client;
import team.four.pas.data.users.Manager;
import team.four.pas.data.users.User;
import java.util.*;
import java.util.stream.Collectors;

/*
Make returns optional instead of Null, Add custom exceptions
 */
public class LocalUserRepository implements UserRepository {

    private final UserMap idLoginMap = new UserMap();

    @Override
    public List<User> getAll() {
        return idLoginMap.valuesAsList();
    }

    @Override
    public User findByLogin(String login) {
        return idLoginMap.get(login);
    }


    @Override
    public List<User> findByLogin(List<String> login) {
        return idLoginMap.getByLogins(login);
    }

    @Override
    public List<User> findByMatchingLogin(String loginStart) {
        List<String> matching = idLoginMap.getLogins()
                                                .stream()
                                                .filter(log -> log.startsWith(loginStart))
                                                .collect(Collectors.toList());
        return idLoginMap.getByLogins(matching);
    }

    @Override
    public User findById(UUID id) {
        return idLoginMap.get(id);
    }

    @Override
    public List<User> findById(List<UUID> ids) {
        return idLoginMap.getByIDs(ids);
    }

    public <T extends User> boolean add(String login, String name, String surname, Class<T> userClass) {
        if(idLoginMap.contains(login) || login.isEmpty()) {
            return false;
        }

        User user;

        do {
            if (userClass == Admin.class) {
                user = new Admin(UUID.randomUUID(), login, name, surname);
            } else if (userClass == Manager.class) {
                user = new Manager(UUID.randomUUID(), login, name, surname);
            } else if (userClass == Client.class) {
                user = new Client(UUID.randomUUID(), login, name, surname);
            } else {
                throw new RuntimeException("Tried adding object which doesn't subclass User");
            }
        } while(idLoginMap.contains(user.getId()));

        idLoginMap.put(user.getLogin(), user);
        return true;
    }

    public boolean update(UUID id, String Surname) {
        if(idLoginMap.contains(id)) {
            idLoginMap.get(id).setSurname(Surname);
            return true;
        }

        return false;
    }

    public boolean update(String login, String Surname) {
        if(idLoginMap.contains(login)) {
            idLoginMap.get(login).setSurname(Surname);
            return true;
        }

        return false;
    }

    @Override
    public boolean delete(UUID id) {
        if(idLoginMap.contains(id)) {
            idLoginMap.remove(id);
            return true;
        }

        return false;
    }

    public boolean delete(String login) {
        if(idLoginMap.contains(login)) {
            idLoginMap.remove(login);
            return true;
        }

        return false;
    }


    private class UserMap {
        Map<UUID, User> idToUser = new HashMap<>();
        Map<String, User> loginToUser  = new HashMap<>();

        List<User> valuesAsList() {
            return idToUser.values().stream().collect(Collectors.toList());
        }

        User get(UUID id) {
            return idToUser.get(id);
        }

        User get(String login) {
            return loginToUser.get(login);
        }

        List<User> getByLogins(List<String> logins) {
            return logins.stream()
                         .map(login -> loginToUser.get(login))
                         .collect(Collectors.toList());
        }

        List<User> getByIDs(List<UUID> logins) {
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

        boolean contains(UUID uuid) {
            return idToUser.containsKey(uuid);
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
