package com.supernova.ssagrissakssak.feed.service;

import com.supernova.ssagrissakssak.core.exception.ErrorCode;
import com.supernova.ssagrissakssak.core.exception.UserRegistrationException;
import com.supernova.ssagrissakssak.feed.persistence.repository.UserRepository;
import com.supernova.ssagrissakssak.feed.persistence.repository.entity.UserEntity;
import com.supernova.ssagrissakssak.fixture.UserFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

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
        UserEntity result = userService.join(UserFixture.initUser());

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getEmail()).isEqualTo(initUser.getEmail());
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
}