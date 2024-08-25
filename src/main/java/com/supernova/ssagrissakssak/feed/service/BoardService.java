package com.supernova.ssagrissakssak.feed.service;

import com.supernova.ssagrissakssak.core.exception.ErrorCode;
import com.supernova.ssagrissakssak.core.exception.SsagriException;
import com.supernova.ssagrissakssak.feed.controller.response.BoardDetailResponse;
import com.supernova.ssagrissakssak.feed.persistence.repository.BoardRepository;
import com.supernova.ssagrissakssak.feed.persistence.repository.entity.BoardEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;

    public BoardDetailResponse getBoard(Long id) {

        BoardEntity board = boardRepository.findById(id).orElseThrow(
                () -> new SsagriException(HttpStatus.NOT_FOUND, ErrorCode.NOT_FOUND));

        /* 조회 수 증가 */
        board.viewCountUp();

        return BoardDetailResponse.from(board);
    }
}
