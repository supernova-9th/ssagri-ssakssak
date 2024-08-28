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
     * @throws BoardNotFoundException 게시물을 찾을 수 없을 때 발생합니다.
     * * @throws SnsApiException SNS API 호출 중 오류가 발생했을 때 발생합니다.
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

    /**
     * 주어진 ID를 기반으로 게시물의 상세 정보를 조회합니다.
     * 게시물을 조회할 때 조회수(view count)가 증가하며, 게시물에 연관된 해시태그들도 함께 조회됩니다.
     *
     * @param id 조회할 게시물의 ID
     * @return 게시물의 상세 정보와 연관된 해시태그들을 포함한 BoardDetailResponse 객체
     * @throws BoardNotFoundException 주어진 ID에 해당하는 게시물을 찾을 수 없는 경우 발생
     */
    public BoardDetailResponse getBoard(Long id) {

        BoardEntity board = boardRepository.findById(id).orElseThrow(
                () -> new BoardNotFoundException(HttpStatus.NOT_FOUND, ErrorCode.NOT_FOUND));

        List<HashtagEntity> hashtags = hashtagRepository.findByBoardId(id);

        board.viewCountUp();

        return BoardDetailResponse.of(board, hashtags);
    }

    /**
     * 주어진 필터링, 정렬, 검색 조건에 따라 게시물 목록을 조회합니다.
     * 조회된 게시물 목록은 BoardResponse 객체로 변환되어 반환됩니다.
     *
     * @param hashtag 게시물에 적용된 해시태그 (선택 사항)
     * @param type 게시물의 타입 (선택 사항)
     * @param orderBy 정렬 기준 필드 (예: "created_at", "view_count", "like_count")
     * @param searchBy 검색할 필드 (예: "title", "content")
     * @param search 검색어 (선택 사항)
     * @param pageCount 페이지당 표시할 게시물 수
     * @param page 조회할 페이지 번호
     * @return 필터링 및 검색 조건에 맞는 게시물 목록을 담은 List<BoardResponse> 객체
     */
    public List<BoardResponse> getBoards(String hashtag, String type, String orderBy, String searchBy,
                                         String search, Integer pageCount, Integer page) {

        List<BoardEntity> boards = boardRepository.getBoards(hashtag, type, orderBy, searchBy, search, pageCount, page);

        return boards.stream()
                .map(BoardResponse::of)
                .collect(Collectors.toList());
    }

}
