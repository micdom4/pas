package team.four.pas.outside;

import team.four.pas.data.AuthResponse;
import team.four.pas.data.users.User;

public interface AuthWebPort {
    AuthResponse register(User request);

    AuthResponse login(String username, String password);

    void changePassword(String oldPassword, String newPassword);

    String generateIntegrityToken(String clientId);

    String generateIntegrityToken(String clientId, String vmId);

    boolean verifyIntegrity(String IntegrityToken, String expectedId);

    boolean verifyIntegrity(String IntegrityToken, String clientId, String vmId);

    void blacklist(String jwt);
}
