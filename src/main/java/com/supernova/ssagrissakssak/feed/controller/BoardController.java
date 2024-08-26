package com.supernova.ssagrissakssak.feed.controller;

import com.supernova.ssagrissakssak.core.security.LoginUser;
import com.supernova.ssagrissakssak.core.wrapper.ResultResponse;
import com.supernova.ssagrissakssak.feed.controller.response.BoardDetailResponse;
import com.supernova.ssagrissakssak.feed.controller.response.BoardResponse;
import com.supernova.ssagrissakssak.feed.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/boards")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    /* 게시물 상세 조회 */
    @GetMapping("/{id}")
    public ResultResponse<BoardDetailResponse> getBoard(@PathVariable Long id, @AuthenticationPrincipal LoginUser loginUser) {
        return new ResultResponse<>(boardService.getBoard(id));
    }

    @GetMapping
    public ResultResponse<List<BoardResponse>> getAllBoard(@AuthenticationPrincipal LoginUser loginUser,
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
}
