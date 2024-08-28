package com.supernova.ssagrissakssak.feed.service;

import com.supernova.ssagrissakssak.client.SnsApiClient;
import com.supernova.ssagrissakssak.core.exception.BoardNotFoundException;
import com.supernova.ssagrissakssak.core.exception.ErrorCode;
import com.supernova.ssagrissakssak.feed.controller.response.BoardDetailResponse;
import com.supernova.ssagrissakssak.feed.controller.response.BoardResponse;
import com.supernova.ssagrissakssak.feed.persistence.repository.BoardRepository;
import com.supernova.ssagrissakssak.feed.persistence.repository.HashtagRepository;
import com.supernova.ssagrissakssak.feed.persistence.repository.entity.BoardEntity;
import com.supernova.ssagrissakssak.feed.persistence.repository.entity.HashtagEntity;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final SnsApiClient snsApiClient;
    private final HashtagRepository hashtagRepository;

    /**
     * 게시물에 좋아요를 추가합니다.
     *
     * @param id 좋아요를 추가할 게시물의 ID
     * @param userId 좋아요를 누른 사용자의 ID
     * @throws BoardNotFoundException 지정된 ID의 게시물을 찾을 수 없는 경우
     */
    @Transactional
    public void addLikeBoardContent(Long id, Long userId) {
        BoardEntity board = boardRepository.findById(id)
                .orElseThrow(BoardNotFoundException::new);

        SnsApiClient.SnsApiResponse response = snsApiClient.callSnsLikeApi(
                new SnsApiClient.SnsApiRequest(board.getType(), board.getId()));

        if (response.isSuccess()) {
            board.incrementLikeCount();
        }
    }

    /**
     * 게시물을 공유합니다.
     *
     * @param id 공유할 게시물의 ID
     * @param userId 공유를 수행한 사용자의 ID
     * @throws BoardNotFoundException 지정된 ID의 게시물을 찾을 수 없는 경우
     */
    @Transactional
    public void shareBoardContent(Long id, Long userId) {
        BoardEntity board = boardRepository.findById(id)
                .orElseThrow(BoardNotFoundException::new);

        SnsApiClient.SnsApiResponse response = snsApiClient.callSnsShareApi(
                new SnsApiClient.SnsApiRequest(board.getType(), board.getId()));

        if (response.isSuccess()) {
            board.incrementShareCount();
        }
    }

    /* 게시물 상세 조회 */
    public BoardDetailResponse getBoard(Long id) {

        BoardEntity board = boardRepository.findById(id).orElseThrow(
                () -> new BoardNotFoundException(HttpStatus.NOT_FOUND, ErrorCode.NOT_FOUND));

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
