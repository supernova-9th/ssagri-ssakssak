package com.supernova.ssagrissakssak.feed.service.delegator;

import com.supernova.ssagrissakssak.feed.controller.request.ApproveRequestModel;
import com.supernova.ssagrissakssak.feed.persistence.repository.entity.UserEntity;
import com.supernova.ssagrissakssak.feed.service.delegator.validator.ActiveStatusValidator;
import com.supernova.ssagrissakssak.feed.service.delegator.validator.PasswordValidator;
import com.supernova.ssagrissakssak.feed.service.delegator.validator.VerificationCodeValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.mockito.Mockito.*;

class ApproveValidateDelegatorTest {

    @Mock
    private ActiveStatusValidator activeStatusValidator;

    @Mock
    private PasswordValidator passwordValidator;

    @Mock
    private VerificationCodeValidator verificationCodeValidator;

    @InjectMocks
    private ApproveValidateDelegator approveValidateDelegator;

    private UserEntity user;
    private ApproveRequestModel approveModel;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        approveValidateDelegator = new ApproveValidateDelegator(List.of(activeStatusValidator, passwordValidator, verificationCodeValidator));

        String email = "test@example.com";
        String password = "encodedPassword";
        String authenticationCode = "ABC123";
        boolean activeStatus = false;
        String differentPassword = "password123";

        user = UserEntity.builder()
                .email(email)
                .password(password)
                .activeStatus(activeStatus)
                .verificationCode(authenticationCode)
                .build();

        approveModel = new ApproveRequestModel(email, differentPassword, authenticationCode);
    }

    @Test
    @DisplayName("모든 validators가 순차적으로 호출되어야 한다.")
    void testValidate() {
        approveValidateDelegator.validate(user, approveModel);

        verify(activeStatusValidator, times(1)).validate(user, approveModel);
        verify(passwordValidator, times(1)).validate(user, approveModel);
        verify(verificationCodeValidator, times(1)).validate(user, approveModel);
    }

    @Test
    @DisplayName("하나의 validator에서 예외가 발생하면 다음 validator는 호출되지 않는다.")
    void testValidateStopsOnException() {
        doThrow(new RuntimeException()).when(activeStatusValidator).validate(user, approveModel);

        try {
            approveValidateDelegator.validate(user, approveModel);
        } catch (RuntimeException ignored) {
        }

        verify(activeStatusValidator, times(1)).validate(user, approveModel);
        verify(passwordValidator, times(0)).validate(user, approveModel);
        verify(verificationCodeValidator, times(0)).validate(user, approveModel);
    }
}
