package com.supernova.ssagrissakssak.feed.service;

import com.supernova.ssagrissakssak.core.exception.ErrorCode;
import com.supernova.ssagrissakssak.core.exception.InvalidVerificationCodeException;
import com.supernova.ssagrissakssak.core.exception.UserNotFoundException;
import com.supernova.ssagrissakssak.core.exception.UserRegistrationException;
import com.supernova.ssagrissakssak.feed.controller.request.AcceptRequestModel;
import com.supernova.ssagrissakssak.feed.persistence.repository.UserRepository;
import com.supernova.ssagrissakssak.feed.persistence.repository.entity.UserEntity;
import com.supernova.ssagrissakssak.feed.service.delegator.AcceptValidateDelegator;
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
    private AcceptValidateDelegator acceptValidateDelegator;

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
    void testAcceptSuccess() {
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

        AcceptRequestModel acceptModel = new AcceptRequestModel(email, password, authenticationCode);

        when(userRepository.findByEmail(acceptModel.email())).thenReturn(Optional.of(user));

        // When
        userService.accept(acceptModel);

        // Then
        verify(acceptValidateDelegator, times(1)).validate(user, acceptModel);
        verify(userRepository, times(1)).findByEmail(acceptModel.email());
        assert user.isActive();
    }

    @Test
    @DisplayName("사용자가 존재하지 않으면 UserNotFoundException을 던진다.")
    void testAcceptUserNotFound() {
        String email = "test@example.com";
        String password = "encodedPassword";
        String authenticationCode = "ABC123";
        boolean activeStatus = false;

        AcceptRequestModel acceptModel = new AcceptRequestModel(email, password, authenticationCode);

        // Given
        when(userRepository.findByEmail(acceptModel.email())).thenReturn(Optional.empty());

        // When & Then
        assertThrows(UserNotFoundException.class, () -> userService.accept(acceptModel));

        verify(acceptValidateDelegator, times(0)).validate(any(), any());
    }

    @Test
    @DisplayName("검증 중 예외가 발생하면 활성화 상태가 변경되지 않는다.")
    void testAcceptValidationFails() {
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

        AcceptRequestModel acceptModel = new AcceptRequestModel(email, password, authenticationCode);

        when(userRepository.findByEmail(acceptModel.email())).thenReturn(Optional.of(user));
        doThrow(new InvalidVerificationCodeException()).when(acceptValidateDelegator).validate(user, acceptModel);

        // When & Then
        assertThrows(InvalidVerificationCodeException.class, () -> userService.accept(acceptModel));

        verify(userRepository, times(1)).findByEmail(acceptModel.email());
        verify(acceptValidateDelegator, times(1)).validate(user, acceptModel);
        assert !user.isActive();
    }
}