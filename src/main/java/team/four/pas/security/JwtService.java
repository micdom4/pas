package team.four.pas.security;

import org.springframework.security.core.userdetails.UserDetails;

import java.util.Map;

public interface JwtService {

    String generateToken(Map<String, Object> extraClaims,
                            UserDetails userDetails);

    String generateToken(UserDetails userDetails);

    String extractUsername(String token);

    boolean isTokenValid(String token, UserDetails userDetails);
}
