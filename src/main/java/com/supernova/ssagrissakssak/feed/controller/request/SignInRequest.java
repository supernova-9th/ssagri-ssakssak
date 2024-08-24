package com.supernova.ssagrissakssak.feed.controller.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record SignInRequest(
        @NotBlank(message = "이메일은 필수입니다.")
        @Email(message = "이메일 형식이 올바르지 않습니다.", regexp = "^[A-Za-z0-9+_.-]+@(.+)$")
        String email,

        @NotBlank(message = "비밀번호는 필수입니다.")
        String password
) {
}