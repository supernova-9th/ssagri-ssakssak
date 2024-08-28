package com.supernova.ssagrissakssak.feed.service;

import com.supernova.ssagrissakssak.core.exception.ErrorCode;
import com.supernova.ssagrissakssak.core.exception.JwtValidateException;
import com.supernova.ssagrissakssak.core.exception.UserNotFoundException;
import com.supernova.ssagrissakssak.core.security.JwtProvider;
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
    public void 토큰을_재발급한다() {
        // Given
        String refreshToken = "refreshToken";
        String email = "test@example.com";

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
        TokenResponse result = tokenService.reissue(refreshToken);

        assertEquals(newTokenResponse, result);
        verify(userRepository, times(1)).findByEmailAndActiveStatusAndRefreshToken(email, true, refreshToken);
        verify(jwtProvider, times(1)).validateToken(refreshToken);
        verify(jwtProvider, times(1)).getEmailFromToken(refreshToken);
        verify(jwtProvider, times(1)).createTokenResponse(email);
    }

    @Test
    public void 유효하지_않은_refresh_token이면_JwtValidateException을_던진다() {
        // Given
        String refreshToken = "refreshToken";

        // When
        doThrow(new JwtValidateException(ErrorCode.INVALID_TOKEN)).when(jwtProvider).validateToken(refreshToken);

        // Then
        assertThrows(JwtValidateException.class, () -> tokenService.reissue(refreshToken));
        verify(jwtProvider, times(1)).validateToken(refreshToken);
        verify(userRepository, never()).findByEmailAndActiveStatusAndRefreshToken(anyString(), anyBoolean(), anyString());
        verify(jwtProvider, never()).getEmailFromToken(anyString());
        verify(jwtProvider, never()).createTokenResponse(anyString());
    }

    @Test
    public void 사용자를_찾을_수_없을_때_토큰_재발급_실패한다() {
        // Given
        String refreshToken = "refreshToken";
        String email = "test@example.com";

        // When
        when(jwtProvider.validateToken(refreshToken)).thenReturn(true);
        when(jwtProvider.getEmailFromToken(refreshToken)).thenReturn(email);
        when(userRepository.findByEmailAndActiveStatusAndRefreshToken(email, true, refreshToken))
                .thenReturn(Optional.empty());

        // Then
        assertThrows(UserNotFoundException.class, () -> tokenService.reissue(refreshToken));
        verify(jwtProvider, times(1)).validateToken(refreshToken);
        verify(jwtProvider, times(1)).getEmailFromToken(refreshToken);
        verify(userRepository, times(1)).findByEmailAndActiveStatusAndRefreshToken(email, true, refreshToken);
        verify(jwtProvider, never()).createTokenResponse(anyString());
    }

}