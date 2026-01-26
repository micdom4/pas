package team.four.pas.services.implementation;


import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import team.four.pas.controllers.DTOs.AuthResponse;
import team.four.pas.repositories.UserRepository;
import team.four.pas.security.JwtService;
import team.four.pas.security.TokenBlackList;
import team.four.pas.services.AuthService;
import team.four.pas.services.data.users.User;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final TokenBlackList tokenBlackList;

    @Override
    public AuthResponse register(User request) {
        User createdUser = userRepository.add(request.getLogin(),
                           passwordEncoder.encode(request.getPassword()),
                           request.getName(),
                           request.getSurname(),
                           request.getClass());

        var jwtToken = jwtService.generateToken(createdUser);
        return new AuthResponse(jwtToken, parseAuthorities(createdUser));
    }

    @Override
    public AuthResponse login(String username, String password) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        username,
                        password
                )
        );

        var user = userRepository.findByLogin(username);
        var jwtToken = jwtService.generateToken(user);

        return new AuthResponse(jwtToken, parseAuthorities(user));
    }

    @Override
    public void changePassword(String oldPassword, String newPassword) {
       Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        var user = (User) authentication.getPrincipal();

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new IllegalStateException("Wrong password");
        }

        tokenBlackList.add(authentication.getCredentials().toString());
        userRepository.updatePassword(user.getId(), passwordEncoder.encode(newPassword));

        SecurityContextHolder.clearContext();
    }

    @Override
    public String generateIntegrityToken(String clientId) {
        return jwtService.generateIntegrityToken(clientId);
    }

    @Override
    public String generateIntegrityToken(String clientId, String vmId) {
        return jwtService.generateIntegrityToken(clientId, vmId);
    }

    @Override
    public boolean verifyIntegrity(String IntegrityToken, String expectedId) {
        System.out.println("Weryfikujemy...");
        return jwtService.verifyIntegrity(IntegrityToken.replace('"', ' ').strip(), expectedId);
    }

    @Override
    public boolean verifyIntegrity(String IntegrityToken, String clientId, String vmId) {
        return jwtService.verifyIntegrity(IntegrityToken, clientId, vmId);
    }

    private List<SimpleGrantedAuthority> parseAuthorities(User user) {
       return (List<SimpleGrantedAuthority>) user.getAuthorities().stream().toList();
    }
}
