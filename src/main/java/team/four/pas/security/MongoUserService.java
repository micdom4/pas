package team.four.pas.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import team.four.pas.repositories.implementation.MongoUserRepository;

@Service
@RequiredArgsConstructor
public class MongoUserService implements UserDetailsService {

    private final MongoUserRepository mongoUserRepository;

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

}
