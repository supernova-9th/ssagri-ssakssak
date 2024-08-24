package com.supernova.ssagrissakssak.feed.controller;

import com.supernova.ssagrissakssak.core.wrapper.ResultResponse;
import com.supernova.ssagrissakssak.feed.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/boards")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    /**
     * 게시물에 좋아요를 추가하는 메서드입니다.
     *
     * @param contentId 좋아요를 추가할 게시물의 ID
     * @return 작업 결과를 담은 ResultResponse 객체
     */
    @PostMapping("/{contentId}/like")
    public ResultResponse<Void> addLikeToBoardContent(@PathVariable String contentId) {
        boardService.addLikeToBoardContent(contentId);
        return new ResultResponse<>();
    }
}
