package team.four.pas.outside;

import team.four.pas.data.users.User;

public interface AuthWebPort {
    team.four.pas.security.AuthResponse register(User request);

    team.four.pas.security.AuthResponse login(String username, String password);

    void changePassword(String oldPassword, String newPassword);

    String generateIntegrityToken(String clientId);

    String generateIntegrityToken(String clientId, String vmId);

    boolean verifyIntegrity(String IntegrityToken, String expectedId);

    boolean verifyIntegrity(String IntegrityToken, String clientId, String vmId);
}
