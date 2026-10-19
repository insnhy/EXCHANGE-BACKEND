package vn.edu.uit.da2.exchange.security;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Service
public class JwtService {

    private final SecretKey signingKey;

    public JwtService(
            @Value("${jwt.secret}") String base64Secret) {

        byte[] keyBytes = Decoders.BASE64.decode(base64Secret);
        this.signingKey = Keys.hmacShaKeyFor(keyBytes);
    }

    // Tao JWT co thoi han 1 gio
    public String generateToken(String username) {

        Instant now = Instant.now();

        return Jwts.builder()
                .subject(username)
                .issuedAt(Date.from(now))
                .expiration(
                    Date.from(now.plus(1, ChronoUnit.HOURS))
                )
                .signWith(signingKey)
                .compact();
    }

    // Lay username tu token hop le
    public String extractUsername(String token) {

        return Jwts.parser()
                .verifyWith(signingKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    // Kiem tra token co hop le voi username hay khong
    public boolean isTokenValid(
            String token, String username) {

        try {
            return username.equals(extractUsername(token));
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}
