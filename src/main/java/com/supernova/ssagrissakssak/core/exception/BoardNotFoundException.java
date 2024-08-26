package com.supernova.ssagrissakssak.core.exception;

import org.springframework.http.HttpStatus;

public class BoardNotFoundException extends SsagriException{

    public BoardNotFoundException() {
        super(HttpStatus.NOT_FOUND, ErrorCode.NOT_FOUND);
    }

    public BoardNotFoundException(ErrorCode errorCode) {
        super(HttpStatus.NOT_FOUND, errorCode);
    }

    public BoardNotFoundException(HttpStatus httpStatus, ErrorCode errorCode) {
        super(httpStatus, errorCode);
    }
}
