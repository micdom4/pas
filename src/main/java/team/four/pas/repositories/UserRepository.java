package team.four.pas.repositories;

import team.four.pas.data.users.User;

interface UserRepository  extends Repository<User> {
    User getByLogin(String login);
}
