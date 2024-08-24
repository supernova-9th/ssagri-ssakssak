package com.supernova.ssagrissakssak.core.exception;


public class BoardNotFoundException extends RuntimeException {

  public BoardNotFoundException(String contentId) {
        super("게시물을 찾을 수 없습니다. ContentId: " + contentId);
    }
}


