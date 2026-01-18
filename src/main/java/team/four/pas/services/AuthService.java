package team.four.pas.services;

import team.four.pas.controllers.DTOs.AuthResponse;
import team.four.pas.services.data.users.User;

public interface AuthService {
    AuthResponse register(User request);

    AuthResponse login(String username, String password);

    void logout(String jwt);
}
