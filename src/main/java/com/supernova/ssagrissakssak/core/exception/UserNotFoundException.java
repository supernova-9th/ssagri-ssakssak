package com.supernova.ssagrissakssak.core.exception;

import org.springframework.http.HttpStatus;

public class UserNotFoundException extends SsagriException {
    public UserNotFoundException() {
        super(HttpStatus.NOT_FOUND, ErrorCode.USER_NOT_FOUND);
    }

    public UserNotFoundException(ErrorCode errorCode) {
        super(HttpStatus.NOT_FOUND, errorCode);
    }

    public UserNotFoundException(HttpStatus httpStatus, ErrorCode errorCode) {
        super(httpStatus, errorCode);
    }
}
