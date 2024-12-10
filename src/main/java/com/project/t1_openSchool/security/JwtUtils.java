package com.project.t1_openSchool.security;

import com.project.t1_openSchool.model.user.Role;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class JwtUtils {

    @Value("${app.jwt.secret}")
    private String secretKey;

    @Value("${app.jwt.expirationTimeAccessToken}")
    private String expirationTimeAccessToken;

    public String generateAccessToken(AppUserPrincipal userDetails) {
        return Jwts.builder()
                .subject(userDetails.getUser().getId().toString())
                .claim("roles", userDetails.getUser().getRoles().stream()
                        .map(Role::getRole).collect(Collectors.toList()))
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + Long.parseLong(expirationTimeAccessToken)))
                .signWith(getSigningKey())
                .compact();
    }

    public boolean isTokenValid(String token) {
        try {
            Jwts.parser().verifyWith(getSigningKey()).build()
                    .parseSignedClaims(token);
            return true;
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error("Expired JWT token {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT token {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty {}", e.getMessage());
        }
        return false;
    }

    public String getId(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public List<String> getRoles(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("roles", List.class);
    }

    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
