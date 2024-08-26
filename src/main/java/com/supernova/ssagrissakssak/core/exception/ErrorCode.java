package com.supernova.ssagrissakssak.core.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    NOT_FOUND("해당 자원을 찾을 수 없습니다."),
    BAD_REQUEST("잘못된 요청입니다."),
    USER_NOT_FOUND("해당 회원를 찾을 수 없습니다."),
    EXPIRED_TOKEN("토큰이 만료되었습니다."),
    INVALID_TOKEN("유효하지 않은 토큰입니다."),
    EMAIL_ALREADY_EXISTS("이미 사용중인 이메일입니다."),
    INVALID_PASSWORD("잘못된 비밀번호입니다."),
    INVALID_VERIFICATION_CODE("인증 코드가 올바르지 않습니다."),
    BOARD_NOT_FOUND("게시물을 찾을 수 없습니다."),
    EXTERNAL_API_ERROR("외부 API 호출 중 오류가 발생했습니다."),
    INVALID_PERIOD("유효하지 않은 기간입니다.")
    ;

    private String defaultMessage;

}
