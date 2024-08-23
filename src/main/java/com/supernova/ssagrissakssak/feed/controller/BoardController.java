package com.supernova.ssagrissakssak.feed.controller;

import com.supernova.ssagrissakssak.core.wrapper.ResultResponse;
import com.supernova.ssagrissakssak.feed.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/boards")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @PostMapping("/{contentId}/like")
    public ResultResponse<Void> boardLike(@PathVariable String contentId) {
        boardService.likeBoardContent(contentId);

        return new ResultResponse<>();
    }

}
