package team.four.pas.services.implementation;


import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import team.four.pas.controllers.DTOs.AuthResponse;
import team.four.pas.controllers.DTOs.UserAddDTO;
import team.four.pas.controllers.DTOs.UserLoginDTO;
import team.four.pas.repositories.UserRepository;
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

    @Override
    public AuthResponse register(User request) {
        User createdUser = userRepository.add(request.getLogin(),
                           passwordEncoder.encode(request.getPassword()),
                           request.getName(),
                           request.getSurname(),
                           request.getClass());

        var jwtToken = jwtService.generateToken(createdUser);
        return new AuthResponse(jwtToken);
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

        return new AuthResponse(jwtToken);
    }
}
