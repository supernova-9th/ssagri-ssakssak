package com.supernova.ssagrissakssak.feed.service;

import com.supernova.ssagrissakssak.core.exception.ErrorCode;
import com.supernova.ssagrissakssak.core.exception.SsagriException;
import com.supernova.ssagrissakssak.feed.controller.response.BoardDetailResponse;
import com.supernova.ssagrissakssak.feed.controller.response.BoardResponse;
import com.supernova.ssagrissakssak.feed.persistence.repository.BoardRepository;
import com.supernova.ssagrissakssak.feed.persistence.repository.HashtagRepository;
import com.supernova.ssagrissakssak.feed.persistence.repository.entity.BoardEntity;
import com.supernova.ssagrissakssak.feed.persistence.repository.entity.HashtagEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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

    public List<BoardResponse> getBoards(String hashtag, String type, String orderBy, String searchBy,
                                         String search, Integer pageCount, Integer page) {

        List<BoardEntity> boards = boardRepository.getBoards(hashtag, type, orderBy, searchBy, search, pageCount, page);

        return boards.stream()
                .map(BoardResponse::of)
                .collect(Collectors.toList());
    }
}
