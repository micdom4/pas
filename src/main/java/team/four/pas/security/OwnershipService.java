package team.four.pas.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import team.four.pas.repositories.AllocationRepository;
import team.four.pas.repositories.UserRepository;
import team.four.pas.services.AuthService;

@Component("ownershipChecker")
@RequiredArgsConstructor
public class OwnershipService {
    private final UserRepository userRepository;
    private final AllocationRepository allocationRepository;
    private final AuthService authService;

    public boolean isOwner(Authentication authentication, String userId) {
        String currentUsername = authentication.getName();
        return userRepository.findById(userId).getLogin().equals(currentUsername);
    }

    public boolean isOwnerOfAllocation(Authentication authentication, String allocationId) {
        String currentUsername = authentication.getName();
        return allocationRepository.findById(allocationId).getClient().getLogin().equals(currentUsername);
    }

    public boolean isValidJws(String clientId, String jws) {
        return authService.verifyIntegrity(jws,  clientId);
    }
}