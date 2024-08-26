package com.supernova.ssagrissakssak.feed.controller;

import com.supernova.ssagrissakssak.core.security.LoginUser;
import com.supernova.ssagrissakssak.core.wrapper.ResultResponse;
import com.supernova.ssagrissakssak.feed.controller.request.BoardStatisticsRequest;
import com.supernova.ssagrissakssak.feed.controller.response.BoardDetailResponse;
import com.supernova.ssagrissakssak.feed.controller.response.StatisticsResponse;
import com.supernova.ssagrissakssak.feed.service.BoardService;
import com.supernova.ssagrissakssak.feed.service.BoardStatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/boards")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;
    private final BoardStatisticsService boardStatisticsService;

    /* 게시물 상세 조회 */
    @GetMapping("/{id}")
    public ResultResponse<BoardDetailResponse> getBoard(@PathVariable Long id) {
        return new ResultResponse<>(boardService.getBoard(id));
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
