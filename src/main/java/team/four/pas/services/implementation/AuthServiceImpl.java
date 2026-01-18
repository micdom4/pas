package team.four.pas.services.implementation;


import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import team.four.pas.controllers.DTOs.AuthResponse;
import team.four.pas.repositories.TokenRepository;
import team.four.pas.repositories.UserRepository;
import team.four.pas.repositories.entities.TokenEntity;
import team.four.pas.security.JwtService;
import team.four.pas.services.AuthService;
import team.four.pas.services.data.users.User;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final TokenRepository tokenRepository;

    @Override
    public AuthResponse register(User request) {
        User createdUser = userRepository.add(
                request.getLogin(),
                passwordEncoder.encode(request.getPassword()),
                request.getName(),
                request.getSurname(),
                request.getClass()
        );

        var jwtToken = jwtService.generateToken(createdUser);
        saveUserToken(createdUser.getLogin(), jwtToken);

        return new AuthResponse(jwtToken);
    }

    @Override
    public AuthResponse login(String username, String password) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );

        var user = userRepository.findByLogin(username);


        var jwtToken = jwtService.generateToken(user);
        saveUserToken(username, jwtToken);

        return new AuthResponse(jwtToken);
    }

    @Override
    public void logout(String jwt) {
        tokenRepository.findFirstByToken(jwt).ifPresent(token -> {
            token.setExpired(true);
            token.setRevoked(true);
            tokenRepository.save(token);
        });

        SecurityContextHolder.clearContext();
    }

    private void saveUserToken(String login, String jwtToken) {
        var token = TokenEntity.builder()
                .userLogin(login)
                .token(jwtToken)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    private void revokeAllUserTokens(String login) {
        var validTokens = tokenRepository.findFirstByUserLogin(login);
        if (validTokens.isEmpty()) return;

        validTokens.forEach(t -> {
            t.setExpired(true);
            t.setRevoked(true);
        });

        tokenRepository.saveAll(validTokens);
    }
}