package com.supernova.ssagrissakssak.feed.controller.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserCreateRequest(
        @NotBlank(message = "이메일은 필수입니다.")
        @Email(message = "이메일 형식이 올바르지 않습니다.", regexp = "^[A-Za-z0-9+_.-]+@(.+)$")
        String email,

        @NotBlank(message = "비밀번호는 필수입니다.")
        @Size(min = 10, message = "비밀번호는 10자 이상이어야 합니다.")
        @Pattern(regexp = ".*[^0-9].*", message = "숫자로만 이루어진 비밀번호는 사용 불가합니다.")
        String password
) {
}