package com.supernova.ssagrissakssak.core.exception;

import org.springframework.http.HttpStatus;

public class ExternalApiException extends SsagriException  {

  public ExternalApiException(String message) {
    super(HttpStatus.INTERNAL_SERVER_ERROR, ErrorCode.EXTERNAL_API_ERROR, "SNS API 호출 중 오류 발생: " + message);
  }

  public ExternalApiException(ErrorCode errorCode, Throwable cause) {
    super(HttpStatus.INTERNAL_SERVER_ERROR, errorCode, errorCode.getDefaultMessage());
    initCause(cause);
  }

}
