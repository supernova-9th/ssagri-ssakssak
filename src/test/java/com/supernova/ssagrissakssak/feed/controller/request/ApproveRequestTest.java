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

class ApproveRequestTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void 이메일_비밀번호_인증_코드가_올바를_경우_request_dto가_생성된다() {
        ApproveRequest request = new ApproveRequest("test@example.com", "password123", "ABC123");
        var violations = validator.validate(request);
        assertThat(violations).isEmpty();
    }

    @ParameterizedTest
    @ValueSource(strings = {"invalid-email", "test@", "@example.com"})
    void 이메일_형식이_올바르지_않을_경우_예외가_발생한다(String email) {
        ApproveRequest request = new ApproveRequest(email, "password123", "ABC123");
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
        ApproveRequest request = new ApproveRequest(email, "password123", "ABC123");
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
        ApproveRequest request = new ApproveRequest("test@example.com", password, "ABC123");
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
    void 비밀번호는_10자_이상이어야_한다(String password) {
        ApproveRequest request = new ApproveRequest("test@example.com", password, "ABC123");
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
    void 비밀번호는_숫자로만_이루어질_수_없다(String password) {
        ApproveRequest request = new ApproveRequest("test@example.com", password, "ABC123");
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
    void 인증_코드는_6자리여야_하며_숫자와_영문자로만_구성되어야_한다(String authenticationCode) {
        ApproveRequest request = new ApproveRequest("test@example.com", "password123", authenticationCode);
        var violations = validator.validate(request);
        assertThat(violations)
                .isNotEmpty()
                .anyMatch(violation ->
                        violation.getPropertyPath().toString().equals("authenticationCode") &&
                                violation.getMessage().contains("인증 코드는 숫자와 영문자로 구성된 6자리여야 합니다.")
                );
    }
}