package com.supernova.ssagrissakssak.feed.controller;

import com.supernova.ssagrissakssak.core.enums.StatType;
import com.supernova.ssagrissakssak.core.enums.StatValueType;
import com.supernova.ssagrissakssak.core.security.LoginUser;
import com.supernova.ssagrissakssak.core.wrapper.ResultResponse;
import com.supernova.ssagrissakssak.feed.controller.response.StatResponse;
import com.supernova.ssagrissakssak.feed.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/boards")
public class BoardController {

    private final BoardService boardService;

    @GetMapping("/stats")
    public ResultResponse<List<StatResponse>> getStats(
            @AuthenticationPrincipal LoginUser user,
            @RequestParam(required = false) String hashtag,
            @RequestParam StatType statType,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end,
            @RequestParam(required = false, defaultValue = "count") StatValueType statValueType
    ) {
        if (hashtag == null) { hashtag = user.getUsername(); }
        if (start == null) { start = LocalDate.now().minusDays(7); }
        if (end == null) { end = LocalDate.now(); }
        return new ResultResponse<>(boardService.getStats(hashtag, statType, start, end, statValueType));
    }
}
