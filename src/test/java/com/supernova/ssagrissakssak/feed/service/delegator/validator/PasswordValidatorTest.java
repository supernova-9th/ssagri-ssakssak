package com.supernova.ssagrissakssak.feed.service.delegator.validator;

import com.supernova.ssagrissakssak.core.exception.InvalidPasswordException;
import com.supernova.ssagrissakssak.feed.controller.request.AcceptRequestModel;
import com.supernova.ssagrissakssak.feed.persistence.repository.entity.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class PasswordValidatorTest {

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private PasswordValidator passwordValidator;

    private UserEntity user;
    private AcceptRequestModel acceptModel;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        String email = "test@example.com";
        String password = "encodedPassword";
        String authenticationCode = "ABC123";
        String differentPassword = "differentPassword";

        user = UserEntity.builder()
                .email(email)
                .password(password)
                .build();

        acceptModel = new AcceptRequestModel("test@example.com", differentPassword, authenticationCode);
    }

    @Test
    @DisplayName("비밀번호가 일치하지 않으면 InvalidPasswordException을 던진다.")
    void testValidateThrowsInvalidPasswordException() {
        String differentPassword = "differentPassword";

        // given
        when(passwordEncoder.encode(acceptModel.password())).thenReturn(differentPassword);

        // then
        assertThrows(InvalidPasswordException.class, () -> {
            passwordValidator.validate(user, acceptModel);
        });

        verify(passwordEncoder, times(1)).encode(acceptModel.password());
    }

    @Test
    @DisplayName("비밀번호가 일치하면 예외가 발생하지 않는다.")
    void testValidateDoesNotThrowException() {
        String password = "encodedPassword";

        // given
        when(passwordEncoder.encode(acceptModel.password())).thenReturn(password);

        // when
        passwordValidator.validate(user, acceptModel);

        // then
        verify(passwordEncoder, times(1)).encode(acceptModel.password());
    }
}
