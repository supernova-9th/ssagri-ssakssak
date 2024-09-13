package com.supernova.ssagrissakssak.feed.service;

import com.supernova.ssagrissakssak.client.SnsApiClient;
import com.supernova.ssagrissakssak.core.exception.BoardNotFoundException;
import com.supernova.ssagrissakssak.core.exception.ErrorCode;
import com.supernova.ssagrissakssak.core.wrapper.PageResponse;
import com.supernova.ssagrissakssak.feed.controller.request.BoardSearchRequest;
import com.supernova.ssagrissakssak.feed.controller.response.BoardDetailResponse;
import com.supernova.ssagrissakssak.feed.controller.response.BoardResponse;
import com.supernova.ssagrissakssak.feed.persistence.repository.BoardRepository;
import com.supernova.ssagrissakssak.feed.persistence.repository.HashtagRepository;
import com.supernova.ssagrissakssak.feed.persistence.repository.entity.BoardEntity;
import com.supernova.ssagrissakssak.feed.persistence.repository.entity.HashtagEntity;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

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
     * 게시물 검색 조건과 페이징 정보를 사용하여 상세 게시물 목록을 조회하고 변환하는 메서드입니다.
     * BoardEntity 목록을 조회한 후, 이를 BoardResponse로 변환하여 페이징된 응답을 반환합니다.
     *
     * @param searchRequest 게시물 검색 조건을 포함하는 객체 (해시태그, 타입, 정렬 기준, 검색 기준, 검색어 등)
     * @param pageRequest 페이징 정보를 포함한 PageRequest 객체 (페이지 번호 및 페이지 크기)
     * @return 검색 및 페이징 조건에 맞는 게시물 목록을 포함한 PageResponse<BoardResponse> 객체를 반환
     */
    public PageResponse<BoardResponse> getAllDetailBoards(BoardSearchRequest searchRequest, PageRequest pageRequest) {

        Page<BoardEntity> boardEntities = boardRepository.getAllBoards(searchRequest, pageRequest);

        // BoardEntity -> BoardResponse 변환
        List<BoardResponse> boardResponses = boardEntities
                .map(BoardResponse::of)
                .getContent();

        return PageResponse.of(boardEntities, boardResponses);
    }

}
