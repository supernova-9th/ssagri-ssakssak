package com.supernova.ssagrissakssak.feed.service;

import com.supernova.ssagrissakssak.core.exception.ErrorCode;
import com.supernova.ssagrissakssak.core.exception.SsagriException;
import com.supernova.ssagrissakssak.feed.controller.response.BoardDetailResponse;
import com.supernova.ssagrissakssak.feed.persistence.repository.BoardRepository;
import com.supernova.ssagrissakssak.feed.persistence.repository.HashtagRepository;
import com.supernova.ssagrissakssak.feed.persistence.repository.entity.BoardEntity;
import com.supernova.ssagrissakssak.feed.persistence.repository.entity.HashtagEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final HashtagRepository hashtagRepository;

    /* 게시물 상세 조회 */
    public BoardDetailResponse getBoard(Long id) {

        BoardEntity board = boardRepository.findById(id).orElseThrow(
                () -> new SsagriException(HttpStatus.NOT_FOUND, ErrorCode.NOT_FOUND));

        List<HashtagEntity> hashtags = hashtagRepository.findByBoardId(id);

        board.viewCountUp();

        return BoardDetailResponse.of(board, hashtags);
    }
}
