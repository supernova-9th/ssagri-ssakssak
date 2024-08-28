package com.supernova.ssagrissakssak.feed.controller;

import com.supernova.ssagrissakssak.core.exception.JwtValidateException;
import com.supernova.ssagrissakssak.core.exception.UserNotFoundException;
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

    /**
     * 토큰을 재발급합니다.
     *
     * @param refreshToken 리프레시 토큰.
     *        HTTP 헤더에 포함되어 전달됩니다.
     * @return 새로운 액세스 토큰과 리프레시 토큰을 포함하는 객체.
     * @throws JwtValidateException 토큰이 만료되었거나 유효하지 않은 경우
     * @throws UserNotFoundException 지정된 email의 사용자를 찾을 수 없는 경우
    */
    @PostMapping("/auth/token/reissue")
    public ResultResponse<TokenResponse> reissue(@RequestHeader(HEADER_AUTHORIZE_TOKEN) String refreshToken) {
        return new ResultResponse<>(tokenService.reissue(refreshToken));
    }

}
