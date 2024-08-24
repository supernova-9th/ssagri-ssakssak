package com.supernova.ssagrissakssak.feed.controller;

import com.supernova.ssagrissakssak.core.exception.BoardNotFoundException;
import com.supernova.ssagrissakssak.core.exception.SnsApiException;
import com.supernova.ssagrissakssak.core.wrapper.ResultResponse;
import com.supernova.ssagrissakssak.feed.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
     * @return 작업 결과를 담은 ResponseEntity 객체
     */
    @PostMapping("/{contentId}/like")
    public ResponseEntity<ResultResponse<Void>> addLikeBoardContent(@PathVariable String contentId) {
        try {
            boardService.addLikeBoardContent(contentId);
            return ResponseEntity.ok(new ResultResponse<>());
        } catch (BoardNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResultResponse<>(HttpStatus.NOT_FOUND, e.getMessage()));
        } catch (SnsApiException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResultResponse<>(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()));
        }
    }
}
