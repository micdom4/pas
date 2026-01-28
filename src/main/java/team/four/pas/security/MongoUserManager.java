package team.four.pas.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;
import team.four.pas.repositories.UserRepository;

@Service
@RequiredArgsConstructor
public class MongoUserManager implements UserDetailsManager {

    private final UserRepository mongoUserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDetails userDetails;

        try {
            userDetails = mongoUserRepository.findByLogin(username);
        } catch (RuntimeException e) {
            throw new UsernameNotFoundException(e.getMessage());
        }

        return userDetails;
    }

    @Override
    public void createUser(UserDetails user) {

    }

    @Override
    public void updateUser(UserDetails user) {
    }

    @Override
    public void deleteUser(String username) {

    }

    @Override
    public void changePassword(String oldPassword, String newPassword) {

    }

    @Override
    public boolean userExists(String username) {
        return false;
    }
}
