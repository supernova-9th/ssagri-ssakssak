package com.supernova.ssagrissakssak.feed.service;

import com.supernova.ssagrissakssak.core.exception.UserNotFoundException;
import com.supernova.ssagrissakssak.core.security.JwtProvider;
import com.supernova.ssagrissakssak.feed.controller.response.TokenResponse;
import com.supernova.ssagrissakssak.feed.persistence.repository.UserRepository;
import com.supernova.ssagrissakssak.feed.persistence.repository.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;

    @Transactional
    public TokenResponse reissue(String refreshToken) {
        jwtProvider.validateToken(refreshToken);
        String email = jwtProvider.getEmailFromToken(refreshToken);
        UserEntity user = userRepository.findByEmailAndActiveStatusAndRefreshToken(email, true, refreshToken).orElseThrow(UserNotFoundException::new);
        TokenResponse token = jwtProvider.createTokenResponse(user.getEmail());
        user.registerRefreshToken(token.refreshToken());
        return token;
    }
}