package Therapie_Management.backend.security;

import java.nio.charset.StandardCharsets;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import Therapie_Management.backend.entity.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

/**
 * Erzeugt und liest JWT-Tokens. Der JwtAuthenticationFilter (Schritt 3.7), der
 * diese Klasse bei jedem Request zur Token-Pruefung nutzt, existiert noch nicht -
 * generateToken() wird aber schon jetzt von AuthService.login() gebraucht.
 *
 * Kein Expiry-Claim gesetzt, da fuer das MVP laut Spezifikation keine Token-Ablaufzeit
 * vorgesehen ist.
 */
@Component
public class JwtTokenProvider {

    private static final String ROLE_CLAIM = "role";

    private final SecretKey signingKey;

    public JwtTokenProvider(@Value("${app.jwt.secret}") String secret) {
        this.signingKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(String userId, Role role) {
        return Jwts.builder()
            .subject(userId)
            .claim(ROLE_CLAIM, role.name())
            .signWith(signingKey)
            .compact();
    }

    public Claims getClaimsFromToken(String token) {
        return Jwts.parser()
            .verifyWith(signingKey)
            .build()
            .parseSignedClaims(token)
            .getPayload();
    }

    public boolean validateToken(String token) {
        try {
            getClaimsFromToken(token);
            return true;
        } catch (JwtException | IllegalArgumentException exception) {
            return false;
        }
    }
}
