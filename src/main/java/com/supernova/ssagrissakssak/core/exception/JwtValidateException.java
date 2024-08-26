package com.supernova.ssagrissakssak.core.exception;

import org.springframework.http.HttpStatus;

public class JwtValidateException extends SsagriException {
    public JwtValidateException(ErrorCode errorCode) {
        super(HttpStatus.UNAUTHORIZED, errorCode);
    }

    public JwtValidateException(HttpStatus httpStatus, ErrorCode errorCode) {
        super(httpStatus, errorCode);
    }
}
