package com.supernova.ssagrissakssak.feed.service;

import com.supernova.ssagrissakssak.core.exception.JwtValidateException;
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

    /**
     * 리프레시 토큰을 사용하여 새로운 액세스 토큰과 리프레시 토큰을 발급합니다.
     * 1. 리프레시 토큰의 유효성 검증
     * 2. 사용자 조회
     * 3. 새로운 토큰 발급
     * 4. 새로운 refresh 토큰 저장
     *
     * @param refreshToken 재발급에 사용할 리프레시 토큰.
     * @return 새로운 액세스 토큰과 리프레시 토큰을 포함하는 `TokenResponse` 객체.
     * @throws JwtValidateException 토큰이 만료되었거나 유효하지 않은 경우
     * @throws UserNotFoundException 지정된 email의 사용자를 찾을 수 없는 경우
     */
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