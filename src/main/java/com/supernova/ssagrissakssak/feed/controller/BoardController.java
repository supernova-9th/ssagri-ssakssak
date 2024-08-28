package com.supernova.ssagrissakssak.feed.controller;

import com.supernova.ssagrissakssak.core.security.LoginUser;
import com.supernova.ssagrissakssak.core.wrapper.ResultResponse;
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
     * 주어진 필터링, 정렬, 검색 조건에 따라 모든 게시물 목록을 조회하여 반환합니다.
     *
     * @param hashtag 게시물에 적용된 해시태그 (선택 사항)
     * @param type 게시물의 타입 (선택 사항)
     * @param orderBy 정렬 기준 필드 (기본값: "created_at")
     * @param searchBy 검색할 필드 (기본값: "title")
     * @param search 검색어 (선택 사항)
     * @param pageCount 페이지당 표시할 게시물 수 (기본값: 10)
     * @param page 조회할 페이지 번호 (기본값: 0)
     * @return 게시물 목록을 포함한 ResultResponse 객체 반환
     */
    @GetMapping
    public ResultResponse<List<BoardResponse>> getAllBoard(
            @RequestParam(value = "hashtag", required = false) String hashtag,
            @RequestParam(value = "type", required = false) String type,
            @RequestParam(value = "orderBy", defaultValue = "created_at") String orderBy,
            @RequestParam(value = "searchBy", defaultValue = "title") String searchBy,
            @RequestParam(value = "search", required = false) String search,
            @RequestParam(value = "pageCount", defaultValue = "10") Integer pageCount,
            @RequestParam(value = "page", defaultValue = "0") Integer page
    ) {
        List<BoardResponse> boardList = boardService.getBoards(
                hashtag, type, orderBy, searchBy, search, pageCount, page
        );

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
