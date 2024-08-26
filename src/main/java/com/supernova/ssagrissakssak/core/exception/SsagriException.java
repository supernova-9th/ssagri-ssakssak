package com.supernova.ssagrissakssak.core.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class SsagriException extends RuntimeException {

    private HttpStatus httpStatus;
    private String message;

    public SsagriException(HttpStatus httpStatus, ErrorCode errorCode) {
        super(errorCode.getDefaultMessage());
        this.httpStatus = httpStatus;
        this.message = errorCode.getDefaultMessage();
    }

    public SsagriException(HttpStatus httpStatus, ErrorCode errorCode, String message) {
        super(errorCode.getDefaultMessage());
        this.httpStatus = httpStatus;
        this.message = errorCode.getDefaultMessage();
    }

}