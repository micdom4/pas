package team.four.pas.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import team.four.pas.inside.AllocationPersistencePort;
import team.four.pas.inside.UserPersistencePort;
import team.four.pas.outside.AuthWebPort;

import java.util.NoSuchElementException;

@Component("ownershipChecker")
@RequiredArgsConstructor
public class OwnershipService {
    private final UserPersistencePort userRepository;
    private final AllocationPersistencePort allocationRepository;
    private final AuthWebPort authService;

    public boolean isOwner(Authentication authentication, String userId) {
        String currentUsername = authentication.getName();
        return userRepository.findById(userId).orElseThrow(() -> new RuntimeException("w")).getLogin().equals(currentUsername);
    }

    public boolean isOwnerOfAllocation(Authentication authentication, String allocationId) {
        String currentUsername = authentication.getName();
        return allocationRepository.findById(allocationId).orElseThrow(NoSuchElementException::new).getClient().getLogin().equals(currentUsername);
    }

    public boolean isValidJws(String clientId, String jws) {
        if (jws == null || jws.isBlank()) return false;

        System.out.println("Sprawdzamy klienta: " + clientId + " jws: " + jws);
        return authService.verifyIntegrity(jws.replace('"', ' ').strip(),  clientId);
    }

    public boolean isValidJws(String clientId, String vmId, String jws) {
        if (jws == null || jws.isBlank()) return false;
        return authService.verifyIntegrity(jws.replace('"', ' ').strip(), clientId,  vmId);
    }
}