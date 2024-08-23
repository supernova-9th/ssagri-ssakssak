package com.supernova.ssagrissakssak.core.exception;

import org.springframework.http.HttpStatus;

public class InvalidVerificationCodeException extends SsagriException {
    public InvalidVerificationCodeException() {
        super(HttpStatus.UNAUTHORIZED, ErrorCode.INVALID_VERIFICATION_CODE);
    }

    public InvalidVerificationCodeException(ErrorCode errorCode) {
        super(HttpStatus.UNAUTHORIZED, errorCode);
    }

    public InvalidVerificationCodeException(HttpStatus httpStatus, ErrorCode errorCode) {
        super(httpStatus, errorCode);
    }
}

