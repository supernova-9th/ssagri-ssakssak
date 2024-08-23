package com.supernova.ssagrissakssak.feed.service.delegator.validator;

import com.supernova.ssagrissakssak.core.exception.InvalidPasswordException;
import com.supernova.ssagrissakssak.feed.controller.request.AcceptRequestModel;
import com.supernova.ssagrissakssak.feed.persistence.repository.entity.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes ={
        PasswordValidator.class,
        BCryptPasswordEncoder.class
})
class PasswordValidatorTest {

    @Autowired
    private PasswordValidator passwordValidator;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private UserEntity user;
    private AcceptRequestModel acceptModel;

    @BeforeEach
    void setUp() {
        String email = "test@example.com";
        String password = passwordEncoder.encode("password");
        String differentPassword = passwordEncoder.encode("differentPassword");
        String authenticationCode = "ABC123";

        user = UserEntity.builder()
                .email(email)
                .password(password)
                .build();

        acceptModel = new AcceptRequestModel(email, differentPassword, authenticationCode);
    }

    @Test
    @DisplayName("비밀번호가 일치하지 않으면 InvalidPasswordException을 던진다.")
    void testValidateThrowsInvalidPasswordException() {
        assertThrows(InvalidPasswordException.class, () -> {
            passwordValidator.validate(user, acceptModel);
        });
    }

    @Test
    @DisplayName("비밀번호가 일치하면 예외가 발생하지 않는다.")
    void testValidateDoesNotThrowException() {
        String email = "test@example.com";
        String password = "password";
        String authenticationCode = "ABC123";
        acceptModel = new AcceptRequestModel(email, password, authenticationCode);

        passwordValidator.validate(user, acceptModel);
    }
}