package com.supernova.ssagrissakssak.core.security;

import com.supernova.ssagrissakssak.core.exception.ErrorCode;
import com.supernova.ssagrissakssak.core.exception.JwtValidateException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

public class JwtProvider {
    private static final String EMAIL = "email";
    private static final String AUTHORIZATION = "Authorization";
    private static final String TOKEN_PREFIX = "Bearer";

    @Value("${security.jwt.secret}")
    private String secretString;

    @Value("${security.jwt.expiration.access}")
    private long accessTokenValidityInMilliseconds;

    @Value("${security.jwt.expiration.refresh}")
    private long refreshTokenValidityInMilliseconds;

    private SecretKey secretKey;

    @PostConstruct
    protected void init() {
        byte[] keyBytes = Decoders.BASE64.decode(secretString);
        this.secretKey = Keys.hmacShaKeyFor(keyBytes);
    }

    public String createAccessToken(String email) {
        return createToken(email, accessTokenValidityInMilliseconds);
    }

    public String createRefreshToken(String email) {
        return createToken(email, refreshTokenValidityInMilliseconds);
    }

    private String createToken(String email, long validityInMilliseconds) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);

        return Jwts.builder()
                .claim(EMAIL, email)
                .issuedAt(now)
                .expiration(validity)
                .signWith(secretKey)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (ExpiredJwtException e) {
            throw new JwtValidateException(ErrorCode.EXPIRED_TOKEN);
        } catch (JwtException e) {
            throw new JwtValidateException(ErrorCode.INVALID_TOKEN);
        }
    }

    public String getEmailFromToken(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
        String email = claims.get(EMAIL, String.class);
        if (email == null) {
            throw new JwtValidateException(ErrorCode.INVALID_TOKEN);
        }
        return email;
    }

    public String extractToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION);
        if (bearerToken != null && bearerToken.startsWith(TOKEN_PREFIX)) {
            return bearerToken.substring(TOKEN_PREFIX.length());
        }
        return null;
    }
}
