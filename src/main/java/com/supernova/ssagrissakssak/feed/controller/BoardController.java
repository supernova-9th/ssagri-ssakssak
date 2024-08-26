package com.supernova.ssagrissakssak.feed.controller;

import com.supernova.ssagrissakssak.core.wrapper.ResultResponse;
import com.supernova.ssagrissakssak.feed.controller.request.BoardStatisticsRequest;
import com.supernova.ssagrissakssak.feed.controller.response.StatisticsResponse;
import com.supernova.ssagrissakssak.feed.service.BoardStatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class BoardController {

    private final BoardStatisticsService boardStatisticsService;

    @GetMapping("/boards/stats")
    public ResultResponse<List<StatisticsResponse>> getBoardStats(BoardStatisticsRequest request) {
        var result = boardStatisticsService.getStatistics(request.hashtag(), request.type(), request.timeType(), request.start(), request.end())
                .stream()
                .map(stat -> StatisticsResponse.of(request.timeType(), stat))
                .toList();

        return new ResultResponse<>(result);
    }
}
