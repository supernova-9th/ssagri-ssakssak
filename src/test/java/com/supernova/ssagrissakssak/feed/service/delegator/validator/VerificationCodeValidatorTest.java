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
    void 인증_코드가_일치하면_예외가_발생하지_않는다() {
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
    void 인증_코드가_일치하지_않으면_InvalidVerificationCodeException을_던진다() {
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