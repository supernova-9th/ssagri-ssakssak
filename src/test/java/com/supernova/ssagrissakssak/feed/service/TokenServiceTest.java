package com.supernova.ssagrissakssak.feed.service;

import com.supernova.ssagrissakssak.core.exception.ErrorCode;
import com.supernova.ssagrissakssak.core.exception.JwtValidateException;
import com.supernova.ssagrissakssak.core.exception.UserNotFoundException;
import com.supernova.ssagrissakssak.core.security.JwtProvider;
import com.supernova.ssagrissakssak.feed.controller.request.RefreshTokenRequest;
import com.supernova.ssagrissakssak.feed.controller.response.TokenResponse;
import com.supernova.ssagrissakssak.feed.persistence.repository.UserRepository;
import com.supernova.ssagrissakssak.feed.persistence.repository.entity.UserEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TokenServiceTest {


    @Mock
    private JwtProvider jwtProvider;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TokenService tokenService;

    @Test
    @DisplayName("토큰을 재발급한다.")
    public void reissue() {
        // Given
        String refreshToken = "refreshToken";
        String email = "test@example.com";
        RefreshTokenRequest refreshTokenRequest = new RefreshTokenRequest(refreshToken);

        UserEntity userEntity = UserEntity.builder().email(email).build();

        String newAccessToken = "newAccessToken";
        String newRefreshToken = "newRefreshToken";
        TokenResponse newTokenResponse = new TokenResponse(newAccessToken, newRefreshToken);

        // When
        when(jwtProvider.validateToken(refreshToken)).thenReturn(true);
        when(jwtProvider.getEmailFromToken(refreshToken)).thenReturn(email);
        when(userRepository.findByEmailAndActiveStatusAndRefreshToken(email, true, refreshToken))
                .thenReturn(Optional.of(userEntity));
        when(jwtProvider.createTokenResponse(email)).thenReturn(newTokenResponse);

        // Then
        TokenResponse result = tokenService.reissue(refreshTokenRequest);

        assertEquals(newTokenResponse, result);
        verify(userRepository, times(1)).findByEmailAndActiveStatusAndRefreshToken(email, true, refreshToken);
        verify(jwtProvider, times(1)).validateToken(refreshToken);
        verify(jwtProvider, times(1)).getEmailFromToken(refreshToken);
        verify(jwtProvider, times(1)).createTokenResponse(email);
    }

    @Test
    @DisplayName("유효하지 않은 refresh token이면 JwtValidateException을 던진다.")
    public void reissueFailsWhenInvalidToken() {
        // Given
        String invalidRefreshToken = "refreshToken";
        RefreshTokenRequest refreshTokenRequest = new RefreshTokenRequest(invalidRefreshToken);

        // When
        doThrow(new JwtValidateException(ErrorCode.INVALID_TOKEN)).when(jwtProvider).validateToken(invalidRefreshToken);

        // Then
        assertThrows(JwtValidateException.class, () -> tokenService.reissue(refreshTokenRequest));
        verify(jwtProvider, times(1)).validateToken(invalidRefreshToken);
        verify(userRepository, never()).findByEmailAndActiveStatusAndRefreshToken(anyString(), anyBoolean(), anyString());
        verify(jwtProvider, never()).getEmailFromToken(anyString());
        verify(jwtProvider, never()).createTokenResponse(anyString());
    }

    @Test
    @DisplayName("사용자를 찾을 수 없을 때 토큰 재발급 실패한다.")
    public void reissueFailsWhenUserNotFound() {
        // Given
        String validRefreshToken = "refreshToken";
        String email = "test@example.com";
        RefreshTokenRequest refreshTokenRequest = new RefreshTokenRequest(validRefreshToken);

        // When
        when(jwtProvider.validateToken(validRefreshToken)).thenReturn(true);
        when(jwtProvider.getEmailFromToken(validRefreshToken)).thenReturn(email);
        when(userRepository.findByEmailAndActiveStatusAndRefreshToken(email, true, validRefreshToken))
                .thenReturn(Optional.empty());

        // Then
        assertThrows(UserNotFoundException.class, () -> tokenService.reissue(refreshTokenRequest));
        verify(jwtProvider, times(1)).validateToken(validRefreshToken);
        verify(jwtProvider, times(1)).getEmailFromToken(validRefreshToken);
        verify(userRepository, times(1)).findByEmailAndActiveStatusAndRefreshToken(email, true, validRefreshToken);
        verify(jwtProvider, never()).createTokenResponse(anyString());
    }

}