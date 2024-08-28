package com.supernova.ssagrissakssak.feed.service.delegator.validator;

import com.supernova.ssagrissakssak.core.exception.UserRegistrationException;
import com.supernova.ssagrissakssak.feed.controller.request.ApproveRequest;
import com.supernova.ssagrissakssak.feed.persistence.repository.entity.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

class ActiveStatusValidatorTest {

    private ActiveStatusValidator activeStatusValidator;

    @BeforeEach
    void setUp() {
        activeStatusValidator = new ActiveStatusValidator();
    }

    @Test void 사용자가_이미_활성화되어_있으면_UserRegistrationException을_던진다() {
        // given
        String email = "test@example.com";
        String encodedPassword = "encodedPassword";
        String authenticationCode = "ABC123";

        UserEntity user = UserEntity.builder()
                .email(email)
                .password(encodedPassword)
                .activeStatus(true)
                .build();

        ApproveRequest approveModel = new ApproveRequest(email, encodedPassword, authenticationCode);

        // then
        assertThrows(UserRegistrationException.class, () -> {
            activeStatusValidator.validate(user, approveModel);
        });
    }

    @Test
    void 사용자가_활성화되지_않은_경우_예외가_발생하지_않는다() {
        // given
        String email = "test@example.com";
        String password = "encodedPassword";
        String authenticationCode = "ABC123";

        UserEntity user = UserEntity.builder()
                .email(email)
                .password(password)
                .activeStatus(false)
                .build();

        ApproveRequest approveModel = new ApproveRequest(email, password, authenticationCode);

        // when
        activeStatusValidator.validate(user, approveModel);
    }
}
