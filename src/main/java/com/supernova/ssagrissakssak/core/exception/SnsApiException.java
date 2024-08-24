package com.supernova.ssagrissakssak.core.exception;

public class SnsApiException extends RuntimeException {

  public SnsApiException(String message) {
    super("SNS API 호출 중 오류 발생: " + message);
  }

  public SnsApiException(String message, Throwable cause) {
    super("SNS API 호출 중 오류 발생: " + message, cause);
  }
}
