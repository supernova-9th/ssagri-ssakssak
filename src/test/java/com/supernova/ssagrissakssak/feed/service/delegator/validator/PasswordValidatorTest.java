package com.supernova.ssagrissakssak.feed.service.delegator.validator;

import com.supernova.ssagrissakssak.core.exception.InvalidPasswordException;
import com.supernova.ssagrissakssak.feed.controller.request.ApproveRequest;
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
    private ApproveRequest approveModel;

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

        approveModel = new ApproveRequest(email, differentPassword, authenticationCode);
    }

    @Test
    void 비밀번호가_일치하지_않으면_InvalidPasswordException을_던진다() {
        assertThrows(InvalidPasswordException.class, () -> {
            passwordValidator.validate(user, approveModel);
        });
    }

    @Test
    void 비밀번호가_일치하면_예외가_발생하지_않는다() {
        String email = "test@example.com";
        String password = "password";
        String authenticationCode = "ABC123";
        approveModel = new ApproveRequest(email, password, authenticationCode);

        passwordValidator.validate(user, approveModel);
    }
}