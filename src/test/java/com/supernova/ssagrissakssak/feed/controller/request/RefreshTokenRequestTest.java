package com.supernova.ssagrissakssak.feed.controller.request;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class RefreshTokenRequestTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " "})
    @DisplayName("refresh token은 비어있을 수 없다.")
    void validateRefreshToken(String refreshToken) {
        RefreshTokenRequest request = new RefreshTokenRequest(refreshToken);
        var violations = validator.validate(request);
        assertThat(violations)
                .isNotEmpty()
                .anyMatch(violation ->
                        violation.getPropertyPath().toString().equals("refreshToken") &&
                                violation.getMessage().contains("refresh token은 필수입니다.")
                );
    }
}