package com.supernova.ssagrissakssak.feed.service;

import com.supernova.ssagrissakssak.core.exception.*;
import com.supernova.ssagrissakssak.core.security.JwtProvider;
import com.supernova.ssagrissakssak.feed.controller.request.ApproveRequest;
import com.supernova.ssagrissakssak.feed.controller.request.SignInRequest;
import com.supernova.ssagrissakssak.feed.controller.response.TokenResponse;
import com.supernova.ssagrissakssak.feed.persistence.repository.UserRepository;
import com.supernova.ssagrissakssak.feed.persistence.repository.entity.UserEntity;
import com.supernova.ssagrissakssak.feed.service.delegator.ApproveValidateDelegator;
import com.supernova.ssagrissakssak.feed.service.delegator.validator.SignInValidator;
import com.supernova.ssagrissakssak.fixture.UserFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private ApproveValidateDelegator approveValidateDelegator;

    @Mock
    private JwtProvider jwtProvider;

    @Mock
    private SignInValidator signInValidator;

    @Test
    @DisplayName("회원가입을 한다.")
    void join() {
        // Given
        UserEntity initUser = UserFixture.initUser();
        String encodedPassword = "encodedPassword";
        UserEntity savedUser = UserFixture.get();
        given(userRepository.existsByEmail(any())).willReturn(false);
        given(passwordEncoder.encode(any())).willReturn(encodedPassword);
        given(userRepository.save(any(UserEntity.class))).willReturn(savedUser);

        // When
        Long result = userService.join(UserFixture.initUser());

        // Then
        assertThat(result).isNotNull();
        then(userRepository).should().existsByEmail(initUser.getEmail());
        then(passwordEncoder).should().encode(initUser.getPassword());
        then(userRepository).should().save(any(UserEntity.class));
    }

    @Test
    @DisplayName("동일한 이메일로 가입한 유저가 있을 경우 회원가입이 되지 않는다.")
    void join2() {
        // Given
        UserEntity initUser = UserFixture.initUser();
        given(userRepository.existsByEmail(initUser.getEmail())).willReturn(true);

        // When & Then
        assertThatThrownBy(() -> userService.join(initUser))
                .isInstanceOf(UserRegistrationException.class)
                .hasMessageContaining(ErrorCode.EMAIL_ALREADY_EXISTS.getDefaultMessage());

        then(userRepository).should().existsByEmail(initUser.getEmail());
        then(passwordEncoder).shouldHaveNoInteractions();
        then(userRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("사용자가 존재하고 모든 검증이 통과되면 활성화 상태가 변경된다.")
    void testApproveSuccess() {
        // Given
        String email = "test@example.com";
        String password = "encodedPassword";
        String authenticationCode = "ABC123";

        UserEntity user = UserEntity.builder()
                .email(email)
                .password(password)
                .activeStatus(false)
                .verificationCode("ABC123")
                .build();

        ApproveRequest approveModel = new ApproveRequest(email, password, authenticationCode);

        when(userRepository.findByEmail(approveModel.email())).thenReturn(Optional.of(user));

        // When
        userService.approve(approveModel);

        // Then
        verify(approveValidateDelegator, times(1)).validate(user, approveModel);
        verify(userRepository, times(1)).findByEmail(approveModel.email());
        assert user.isActive();
    }

    @Test
    @DisplayName("사용자가 존재하지 않으면 UserNotFoundException을 던진다.")
    void testApproveUserNotFound() {
        String email = "test@example.com";
        String password = "encodedPassword";
        String authenticationCode = "ABC123";
        boolean activeStatus = false;

        ApproveRequest approveModel = new ApproveRequest(email, password, authenticationCode);

        // Given
        when(userRepository.findByEmail(approveModel.email())).thenReturn(Optional.empty());

        // When & Then
        assertThrows(UserNotFoundException.class, () -> userService.approve(approveModel));

        verify(approveValidateDelegator, times(0)).validate(any(), any());
    }

    @Test
    @DisplayName("검증 중 예외가 발생하면 활성화 상태가 변경되지 않는다.")
    void testApproveValidationFails() {
        // Given
        String email = "test@example.com";
        String password = "encodedPassword";
        String authenticationCode = "ABC123";
        boolean activeStatus = false;

        UserEntity user = UserEntity.builder()
                .email(email)
                .password(password)
                .activeStatus(activeStatus)
                .verificationCode("ABC123")
                .build();

        ApproveRequest approveModel = new ApproveRequest(email, password, authenticationCode);

        when(userRepository.findByEmail(approveModel.email())).thenReturn(Optional.of(user));
        doThrow(new InvalidVerificationCodeException()).when(approveValidateDelegator).validate(user, approveModel);

        // When & Then
        assertThrows(InvalidVerificationCodeException.class, () -> userService.approve(approveModel));

        verify(userRepository, times(1)).findByEmail(approveModel.email());
        verify(approveValidateDelegator, times(1)).validate(user, approveModel);
        assert !user.isActive();
    }

    @Test
    @DisplayName("로그인을 한다.")
    void signInSuccessTest() throws Exception {
        // given
        String email = "test@email.com";
        String password = "password123";
        SignInRequest request = new SignInRequest(email, password);
        UserEntity user = UserEntity.builder().email(email).build();

        String accessToken = "access-token";
        String refreshToken = "refresh-token";
        TokenResponse token = new TokenResponse(accessToken, refreshToken);

        given(userRepository.findByEmailAndActiveStatus(email, true)).willReturn(Optional.of(user));
        given(jwtProvider.createTokenResponse(email)).willReturn(token);

        // when
        TokenResponse result = userService.signIn(request);

        // then
        verify(signInValidator, times(1)).validate(user, request);
        verify(userRepository, times(1)).findByEmailAndActiveStatus(email, true);
        verify(jwtProvider, times(1)).createTokenResponse(email);
        assertEquals(accessToken, result.accessToken());
        assertEquals(refreshToken, result.refreshToken());
    }

    @Test
    @DisplayName("로그인시 활성화된 사용자가 아닐때 실패한다.")
    void signInFailsWhenUserNotFound() {
        // given
        String email = "test@email.com";
        SignInRequest request = new SignInRequest(email, "password123");

        given(userRepository.findByEmailAndActiveStatus(email, true)).willReturn(Optional.empty());

        // when & then
        assertThrows(UserNotFoundException.class, () -> userService.signIn(request));
        verify(signInValidator, never()).validate(any(), any());
        verify(jwtProvider, never()).createTokenResponse(any());
    }

    @Test
    @DisplayName("비밀번호가 틀리면 로그인을 실패한다.")
    void signInFailsWhenMismatchPassword() {
        // given
        String email = "test@email.com";
        String password = "wrongPassword";
        SignInRequest signInRequest = new SignInRequest(email, password);
        UserEntity user = UserEntity.builder().email(email).build();

        doThrow(new InvalidPasswordException()).when(signInValidator).validate(user, signInRequest);

        // when & then
        assertThrows(InvalidPasswordException.class, () -> signInValidator.validate(user, signInRequest));
        verify(jwtProvider, never()).createTokenResponse(any());
    }
}