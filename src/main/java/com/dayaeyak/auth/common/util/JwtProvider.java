package com.dayaeyak.auth.common.util;

import com.dayaeyak.auth.domain.auth.enums.ProviderType;
import com.dayaeyak.auth.domain.auth.enums.UserRole;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtProvider {

    private static final String TOKEN_ROLE_CLAIM = "role";

    @Value("${jwt.access.expiration}")
    private Long accessExpiration;

    @Value("${jwt.refresh.expiration}")
    private Long refreshExpiration;

    private SecretKey accessSecretKey;
    private SecretKey refreshSecretKey;

    public JwtProvider(
            @Value("${jwt.access.key}") String accessKey,
            @Value("${jwt.refresh.key}") String refreshKey
    ) {
        this.accessSecretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(accessKey));
        this.refreshSecretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(refreshKey));
    }

    public String generateAccessToken(Long userId, UserRole role) {
        return Jwts.builder()
                .subject(userId.toString())
                .claim(TOKEN_ROLE_CLAIM, role)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + accessExpiration))
                .signWith(accessSecretKey)
                .compact();
    }

    public String generateRefreshToken(Long userId) {
        return Jwts.builder()
                .subject(userId.toString())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + refreshExpiration))
                .signWith(refreshSecretKey)
                .compact();
    }

    public String generateRegistrationToken(Long userId, ProviderType providerType, String providerId) {
        return Jwts.builder()
                .subject(userId.toString())
                .claim("providerType", providerType)
                .claim("providerId", providerId)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + accessExpiration))
                .compact();
    }
}
