package team.four.pas.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {

    @Value("${jwt.secret}")
    private String SECRET_KEY;

    private final TokenBlackList blacklist;

    @Value("${jwt.time}")
    private Integer timeout;

    public String generateToken(Map<String, Object> extraClaims,
                                UserDetails userDetails) {
        return Jwts.builder()
                   .setClaims(extraClaims)
                   .subject(userDetails.getUsername())
                   .setIssuedAt(new Date(System.currentTimeMillis()))
                   .setExpiration(new Date(System.currentTimeMillis() + timeout))
                   .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                   .compact();
    }

    public String generateIntegrityToken(String objectId) {
        return Jwts.builder()
                .setSubject(objectId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 600000))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateIntegrityToken(String objectId, String vmId) {
        return Jwts.builder()
                .setSubject(objectId + ":" + vmId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 600000))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean verifyIntegrity(String token, String expectedId) {
        try {
            String idFromToken = extractUsername(token);
            boolean returnVal = idFromToken.equals(expectedId) && !isTokenExpired(token) && !blacklist.contains(token);
            blacklist.add(token);

            return returnVal;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean verifyIntegrity(String token, String expectedClientId, String expectedVmId) {
        try {
            Claims claims = extractAllClaims(token);

            String compositeKey = claims.getSubject();
            String[] parts = compositeKey.split(":");

            if (parts.length != 2) return false;

            boolean returnVal = parts[0].equals(expectedClientId) && parts[1].equals(expectedVmId) && !blacklist.contains(token);
            blacklist.add(token);

            return returnVal;
        } catch (Exception e) {
            return false;
        }
    }

    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
   }

   public boolean isTokenValid(String token, UserDetails userDetails) {
        final String UserName = extractUsername(token);
        return userDetails.getUsername().equals(UserName) && !isTokenExpired(token) && !blacklist.contains(token);
   }

   public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
   }

   public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
   }


    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);

        return claimsResolver.apply(claims);
    }

    public Claims extractAllClaims(String token) {
        return Jwts.parser()
                   .setSigningKey(getSignInKey())
                   .build()
                   .parseClaimsJws(token)
                   .getBody();
    }

    public Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}
