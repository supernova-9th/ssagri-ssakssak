package com.supernova.ssagrissakssak.feed.controller;

import com.supernova.ssagrissakssak.core.security.LoginUser;
import com.supernova.ssagrissakssak.core.wrapper.PageResponse;
import com.supernova.ssagrissakssak.core.wrapper.ResultResponse;
import com.supernova.ssagrissakssak.feed.controller.request.BoardSearchRequest;
import com.supernova.ssagrissakssak.feed.controller.request.BoardStatisticsRequest;
import com.supernova.ssagrissakssak.feed.controller.response.BoardDetailResponse;
import com.supernova.ssagrissakssak.feed.controller.response.BoardResponse;
import com.supernova.ssagrissakssak.feed.controller.response.StatisticsResponse;
import com.supernova.ssagrissakssak.feed.service.BoardService;
import com.supernova.ssagrissakssak.feed.service.BoardStatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/boards")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;
    private final BoardStatisticsService boardStatisticsService;

    /**
     * 주어진 ID에 해당하는 게시물의 상세 정보를 조회하여 반환합니다.
     *
     * @param id 조회할 게시물의 ID
     * @return 게시물의 상세 정보를 포함한 ResultResponse 객체 반환
     */
    @GetMapping("/{id}")
    public ResultResponse<BoardDetailResponse> getBoard(@PathVariable Long id) {
        return new ResultResponse<>(boardService.getBoard(id));
    }

    /**
     * 주어진 검색 조건 및 페이징 정보를 사용하여 게시물 목록을 조회하고, 결과를 반환합니다.
     *
     * @param searchRequest 게시물 검색 조건을 포함하는 객체 (해시태그, 타입, 정렬 기준, 검색어 및 페이징 정보 포함)
     * @return 검색 및 페이징 조건에 맞는 게시물 목록을 포함한 ResultResponse 객체를 반환합니다.
     *         ResultResponse<PageResponse<BoardResponse>> 형식으로, 페이징된 게시물 목록 반환
     */
    @GetMapping()
    public ResultResponse<PageResponse<BoardResponse>> getAllSearchBoards(@ModelAttribute BoardSearchRequest searchRequest) {
        // 쿼리 파라미터 & 페이징 요청 정보 전달
        PageResponse<BoardResponse> boardList = boardService.getAllDetailBoards(searchRequest, searchRequest.toPageRequest());

        return new ResultResponse<>(boardList);
    }

    /**
     * 게시물에 좋아요를 추가하는 메서드입니다.
     *
     * @param id 좋아요를 추가할 게시물의 ID
     * @return 작업 결과를 담은 ResultResponse 객체
     */
    @PostMapping("/{id}/like")
    public ResultResponse<Void> addLikeBoardContent(@PathVariable Long id, @AuthenticationPrincipal LoginUser loginUser) {
        boardService.addLikeBoardContent(id, loginUser.getId());
        return new ResultResponse<>();
    }

    @PostMapping("/{id}/share")
    public ResultResponse<Void> addShareBoardContent(@PathVariable Long id, @AuthenticationPrincipal LoginUser loginUser) {
        boardService.shareBoardContent(id, loginUser.getId());
        return new ResultResponse<>();
    }

    /* 게시물 통계 조회 */
    @GetMapping("/stats")
    public ResultResponse<List<StatisticsResponse>> getBoardStats(BoardStatisticsRequest request) {
        var result = boardStatisticsService.getStatistics(request.hashtag(), request.type(), request.timeType(), request.start(), request.end())
                .stream()
                .map(stat -> StatisticsResponse.of(request.timeType(), stat))
                .toList();

        return new ResultResponse<>(result);
    }

}
