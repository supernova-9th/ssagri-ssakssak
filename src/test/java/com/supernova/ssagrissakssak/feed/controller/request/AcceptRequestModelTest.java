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

class AcceptRequestModelTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("이메일, 비밀번호, 인증 코드가 올바를 경우 request dto가 생성된다.")
    void testValidAcceptRequestModel() {
        AcceptRequestModel request = new AcceptRequestModel("test@example.com", "password123", "ABC123");
        var violations = validator.validate(request);
        assertThat(violations).isEmpty();
    }

    @ParameterizedTest
    @ValueSource(strings = {"invalid-email", "test@", "@example.com"})
    @DisplayName("이메일 형식이 올바르지 않을 경우 예외가 발생한다.")
    void emailValidate(String email) {
        AcceptRequestModel request = new AcceptRequestModel(email, "password123", "ABC123");
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
    @DisplayName("이메일은 비어있을 수 없다.")
    void emailValidate2(String email) {
        AcceptRequestModel request = new AcceptRequestModel(email, "password123", "ABC123");
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
    @DisplayName("비밀번호는 비어있을 수 없다.")
    void passwordValidate(String password) {
        AcceptRequestModel request = new AcceptRequestModel("test@example.com", password, "ABC123");
        var violations = validator.validate(request);
        assertThat(violations)
                .isNotEmpty()
                .anyMatch(violation ->
                        violation.getPropertyPath().toString().equals("password") &&
                                violation.getMessage().contains("비밀번호는 필수입니다.")
                );
    }

    @ParameterizedTest
    @ValueSource(strings = {"short", "123456789"})
    @DisplayName("비밀번호는 10자 이상이어야 한다.")
    void passwordValidate2(String password) {
        AcceptRequestModel request = new AcceptRequestModel("test@example.com", password, "ABC123");
        var violations = validator.validate(request);
        assertThat(violations)
                .isNotEmpty()
                .anyMatch(violation ->
                        violation.getPropertyPath().toString().equals("password") &&
                                violation.getMessage().contains("비밀번호는 10자 이상이어야 합니다.")
                );
    }

    @ParameterizedTest
    @ValueSource(strings = {"123456789"})
    @DisplayName("비밀번호는 숫자로만 이루어질 수 없다.")
    void passwordValidate3(String password) {
        AcceptRequestModel request = new AcceptRequestModel("test@example.com", password, "ABC123");
        var violations = validator.validate(request);
        assertThat(violations)
                .isNotEmpty()
                .anyMatch(violation ->
                        violation.getPropertyPath().toString().equals("password") &&
                                violation.getMessage().contains("숫자로만 이루어진 비밀번호는 사용 불가합니다.")
                );
    }

    @ParameterizedTest
    @ValueSource(strings = {"ABC12", "A1B2C3D4", "A1@3B6"})
    @DisplayName("인증 코드는 6자리여야 하며, 숫자와 영문자로만 구성되어야 한다.")
    void authenticationCodeValidate(String authenticationCode) {
        AcceptRequestModel request = new AcceptRequestModel("test@example.com", "password123", authenticationCode);
        var violations = validator.validate(request);
        assertThat(violations)
                .isNotEmpty()
                .anyMatch(violation ->
                        violation.getPropertyPath().toString().equals("authenticationCode") &&
                                violation.getMessage().contains("인증 코드는 숫자와 영문자로 구성된 6자리여야 합니다.")
                );
    }
}