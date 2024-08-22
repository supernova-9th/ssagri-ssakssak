package com.supernova.ssagrissakssak.core.exception;

import org.springframework.http.HttpStatus;

public class UserRegistrationException extends SsagriException {
    public UserRegistrationException() {
        super(HttpStatus.CONFLICT, ErrorCode.EMAIL_ALREADY_EXISTS);
    }
    public UserRegistrationException(ErrorCode errorCode) {
        super(HttpStatus.UNAUTHORIZED, errorCode);
    }

    public UserRegistrationException(HttpStatus httpStatus, ErrorCode errorCode) {
        super(httpStatus, errorCode);
    }
}
