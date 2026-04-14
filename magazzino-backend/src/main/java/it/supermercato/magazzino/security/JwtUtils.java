package it.supermercato.magazzino.security;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtils {

    // Usually you retrieve this from properties. Generating a secure key for prototype.
    private final Key jwtSigningKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    
    // Token validity (e.g. 1 hour)
    private final long jwtExpirationMs = 3600000;

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(jwtSigningKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Generates a new JWT token for a specific user after successful authentication.
     * The token contains standard claims (issuedAt, expiration) and sets the subject to the username.
     * It is signed using the server's secret HS256 key to prevent tampering.
     *
     * @param userDetails The authenticated user details object.
     * @return The raw JWT string.
     */
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userDetails.getUsername());
    }

    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(jwtSigningKey)
                .compact();
    }

    /**
     * Validates an incoming JWT token against the current requested User.
     * It parses the token to extract the username and checks if it matches the DB record,
     * ensuring that the token hasn't reached its internal expiration timeframe.
     *
     * @param token The raw JWT string.
     * @param userDetails The domain user it claims to belong to.
     * @return True if valid and not expired, false otherwise.
     */
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}
