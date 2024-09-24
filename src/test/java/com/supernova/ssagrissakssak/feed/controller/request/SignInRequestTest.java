package com.supernova.ssagrissakssak.feed.controller.request;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

class SignInRequestTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void 이메일과_비밀번호_형식이_올바를_경우_request_dto가_생성된다() {
        SignInRequest request = new SignInRequest("test@example.com", "password123");
        var violations = validator.validate(request);
        assertThat(violations).isEmpty();
    }

    @ParameterizedTest
    @ValueSource(strings = {"invalid-email", "test@", "@example.com"})
    void 이메일_형식이_올바르지_않을_경우_예외가_발생한다(String email) {
        SignInRequest request = new SignInRequest(email, "password123");
        var violations = validator.validate(request);
        assertThat(violations)
                .isNotEmpty()
                .anyMatch(violation ->
                        violation.getPropertyPath().toString().equals("email") &&
                                violation.getMessage().contains("이메일 형식이 올바르지 않습니다.")
                );
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " "})
    void 이메일은_비어있을_수_없다(String email) {
        SignInRequest request = new SignInRequest(email, "password123");
        var violations = validator.validate(request);
        assertThat(violations)
                .isNotEmpty()
                .anyMatch(violation ->
                        violation.getPropertyPath().toString().equals("email") &&
                                violation.getMessage().contains("이메일은 필수입니다.")
                );
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " "})
    void 비밀번호는_비어있을_수_없다(String password) {
        SignInRequest request = new SignInRequest("test@example.com", password);
        var violations = validator.validate(request);
        assertThat(violations)
                .isNotEmpty()
                .anyMatch(violation ->
                        violation.getPropertyPath().toString().equals("password") &&
                                violation.getMessage().contains("비밀번호는 필수입니다.")
                );
    }
}