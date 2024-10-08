package com.supernova.ssagrissakssak.core.handler;

import com.supernova.ssagrissakssak.core.exception.BoardNotFoundException;
import com.supernova.ssagrissakssak.core.exception.ExternalApiException;
import com.supernova.ssagrissakssak.core.exception.SsagriException;
import com.supernova.ssagrissakssak.core.wrapper.ResultResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(SsagriException.class)
    public ResultResponse<Void> handleWantedException(SsagriException ex) {
        return new ResultResponse<>(ex.getHttpStatus(), ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResultResponse<Void> handleValidationExceptions(MethodArgumentNotValidException ex) {
        StringBuilder sb = new StringBuilder();
        ex.getBindingResult().getAllErrors().forEach(x -> sb.append(x).append("\n"));
        return new ResultResponse<>(HttpStatus.BAD_REQUEST, sb.toString().trim());
    }

    @ExceptionHandler(value = Exception.class)
    public ResultResponse<Void> unhandledException(Exception e, HttpServletRequest request) {
        log.error("error occur {}", e);
        return new ResultResponse<>(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    }

    @ExceptionHandler(BoardNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResultResponse<Void> handleBoardNotFoundException(BoardNotFoundException e, HttpServletRequest request) {
        log.error("Board not found exception occurred: {}", e.getMessage());
        return new ResultResponse<>(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(ExternalApiException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResultResponse<Void> handleSnsApiException(ExternalApiException e, HttpServletRequest request) {
        log.error("SNS API exception occurred: {}", e.getMessage());
        return new ResultResponse<>(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    }

}
