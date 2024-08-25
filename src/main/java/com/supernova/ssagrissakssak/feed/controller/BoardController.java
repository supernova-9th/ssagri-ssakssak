package com.supernova.ssagrissakssak.feed.controller;

import com.supernova.ssagrissakssak.core.wrapper.ResultResponse;
import com.supernova.ssagrissakssak.feed.controller.response.BoardDetailResponse;
import com.supernova.ssagrissakssak.feed.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/boards")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    /* 게시물 상세 조회 */
    @GetMapping("/{id}")
    public ResultResponse<BoardDetailResponse> getBoard(@PathVariable Long id) {
        return new ResultResponse<>(boardService.getBoard(id));
    }
}
