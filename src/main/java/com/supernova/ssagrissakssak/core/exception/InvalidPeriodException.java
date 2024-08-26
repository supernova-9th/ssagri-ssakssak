package com.supernova.ssagrissakssak.core.exception;

import org.springframework.http.HttpStatus;

public class InvalidPeriodException extends SsagriException {
    public InvalidPeriodException() {
        super(HttpStatus.BAD_REQUEST, ErrorCode.INVALID_PERIOD);
    }

    public InvalidPeriodException(ErrorCode errorCode) {
        super(HttpStatus.BAD_REQUEST, errorCode);
    }

    public InvalidPeriodException(HttpStatus httpStatus, ErrorCode errorCode) {
        super(httpStatus, errorCode);
    }
}
