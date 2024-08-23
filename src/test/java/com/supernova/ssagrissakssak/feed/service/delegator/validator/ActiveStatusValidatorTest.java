package com.supernova.ssagrissakssak.feed.service.delegator.validator;

import com.supernova.ssagrissakssak.core.exception.UserRegistrationException;
import com.supernova.ssagrissakssak.feed.controller.request.AcceptRequestModel;
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

    @Test
    @DisplayName("사용자가 이미 활성화되어 있으면 UserRegistrationException을 던진다.")
    void testValidateThrowsUserRegistrationException() {
        // given
        String email = "test@example.com";
        String encodedPassword = "encodedPassword";
        String authenticationCode = "ABC123";

        UserEntity user = UserEntity.builder()
                .email(email)
                .password(encodedPassword)
                .activeStatus(true)
                .build();

        AcceptRequestModel acceptModel = new AcceptRequestModel(email, encodedPassword, authenticationCode);

        // then
        assertThrows(UserRegistrationException.class, () -> {
            activeStatusValidator.validate(user, acceptModel);
        });
    }

    @Test
    @DisplayName("사용자가 활성화되지 않은 경우 예외가 발생하지 않는다.")
    void testValidateDoesNotThrowException() {
        // given
        String email = "test@example.com";
        String password = "encodedPassword";
        String authenticationCode = "ABC123";

        UserEntity user = UserEntity.builder()
                .email(email)
                .password(password)
                .activeStatus(false)
                .build();

        AcceptRequestModel acceptModel = new AcceptRequestModel(email, password, authenticationCode);

        // when
        activeStatusValidator.validate(user, acceptModel);
    }
}
