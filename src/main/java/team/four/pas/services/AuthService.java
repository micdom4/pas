package team.four.pas.services;

import org.springframework.security.core.userdetails.UserDetails;
import team.four.pas.controllers.DTOs.AuthResponse;
import team.four.pas.controllers.DTOs.UserAddDTO;
import team.four.pas.controllers.DTOs.UserLoginDTO;
import team.four.pas.services.data.users.User;

public interface AuthService {
    AuthResponse register(User request);

    AuthResponse login(String username, String password);

    void changePassword(String oldPassword, String newPassword);

    String generateIntegrityToken(String clientId);

    String generateIntegrityToken(String clientId, String vmId);

    boolean verifyIntegrity(String IntegrityToken, String expectedId);

    boolean verifyIntegrity(String IntegrityToken, String clientId, String vmId);

    AuthResponse resetToken(String token);
}
