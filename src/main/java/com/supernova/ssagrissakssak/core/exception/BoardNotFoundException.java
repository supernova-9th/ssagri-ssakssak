package com.supernova.ssagrissakssak.core.exception;


import org.springframework.http.HttpStatus;

public class BoardNotFoundException extends SsagriException   {

    public BoardNotFoundException(Long id) {
        super(HttpStatus.NOT_FOUND, ErrorCode.BOARD_NOT_FOUND, "게시물을 찾을 수 없습니다. Id: " + id);
    }

}