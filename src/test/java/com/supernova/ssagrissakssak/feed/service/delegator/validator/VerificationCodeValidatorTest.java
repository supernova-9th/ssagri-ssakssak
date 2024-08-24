package com.supernova.ssagrissakssak.feed.service.delegator.validator;

import com.supernova.ssagrissakssak.core.exception.InvalidVerificationCodeException;
import com.supernova.ssagrissakssak.feed.controller.request.ApproveRequest;
import com.supernova.ssagrissakssak.feed.persistence.repository.entity.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VerificationCodeValidatorTest {


    private VerificationCodeValidator verificationCodeValidator;

    @BeforeEach
    void setUp() {
        verificationCodeValidator = new VerificationCodeValidator();
    }

    @Test
    @DisplayName("인증 코드가 일치하면 예외가 발생하지 않는다.")
    void testValidateThrowsInvalidVerificationCodeException() {
        String email = "test@example.com";
        String password = "encodedPassword";
        String authenticationCode = "ABC123";

        // given
        UserEntity user = UserEntity.builder()
                .email(email)
                .password(password)
                .verificationCode(authenticationCode)
                .build();

        ApproveRequest approveModel = new ApproveRequest(email, password, "ABC123");

        verificationCodeValidator.validate(user, approveModel);
    }

    @Test
    @DisplayName("인증 코드가 일치하지 않으면 InvalidVerificationCodeException을 던진다.")
    void testValidateDoesNotThrowException() {
        String email = "test@example.com";
        String password = "encodedPassword";
        String authenticationCode = "ABC123";
        String differentAuthenticationCode = "XYZ789";

        // given
        UserEntity user = UserEntity.builder()
                .email(email)
                .password(password)
                .verificationCode(authenticationCode)
                .build();

        ApproveRequest approveModel = new ApproveRequest(email, password, differentAuthenticationCode);

        // then
        assertThrows(InvalidVerificationCodeException.class, () -> {
            verificationCodeValidator.validate(user, approveModel);
        });
    }
}