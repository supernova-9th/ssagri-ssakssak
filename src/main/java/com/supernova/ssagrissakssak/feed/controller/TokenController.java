package com.supernova.ssagrissakssak.feed.controller;

import com.supernova.ssagrissakssak.core.wrapper.ResultResponse;
import com.supernova.ssagrissakssak.feed.controller.response.TokenResponse;
import com.supernova.ssagrissakssak.feed.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import static com.supernova.ssagrissakssak.core.constants.CommonConstant.HEADER_AUTHORIZE_TOKEN;

@RestController
@RequiredArgsConstructor
public class TokenController {

    private final TokenService tokenService;

    @PostMapping("/auth/token/reissue")
    public ResultResponse<TokenResponse> reissue(@RequestHeader(HEADER_AUTHORIZE_TOKEN) String refreshToken) {
        return new ResultResponse<>(tokenService.reissue(refreshToken));
    }

}
