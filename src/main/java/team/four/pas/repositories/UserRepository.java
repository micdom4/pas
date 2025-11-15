package team.four.pas.repositories;

import team.four.pas.data.users.User;

import java.util.UUID;

public interface UserRepository {

    boolean addUser();

    // R
    User getUser(UUID id);

    boolean updateUser(UUID id);

    boolean deleteUser(UUID id);
}
