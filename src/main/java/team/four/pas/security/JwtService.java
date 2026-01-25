package team.four.pas.security;

import org.springframework.security.core.userdetails.UserDetails;

import java.util.Map;

public interface JwtService {

    String generateAccessToken(Map<String, Object> extraClaims,
                               UserDetails userDetails);

    String generateAccessToken(UserDetails userDetails);

    String generateRefreshToken(UserDetails userDetails);

    String extractUsername(String token);

    String extractRefresh(String token);

    boolean isTokenValid(String token, UserDetails userDetails);
    boolean isTokenValid(String token);


    String generateIntegrityToken(String objectId);
    String generateIntegrityToken(String objectId, String vmId);

    boolean verifyIntegrity(String token, String expectedId);
    boolean verifyIntegrity(String token, String expectedId, String vmId);
}
