package com.supernova.ssagrissakssak.feed.controller;

import com.supernova.ssagrissakssak.core.wrapper.ResultResponse;
import com.supernova.ssagrissakssak.feed.controller.request.ApproveRequest;
import com.supernova.ssagrissakssak.feed.controller.request.SignInRequest;
import com.supernova.ssagrissakssak.feed.controller.request.UserCreateRequest;
import com.supernova.ssagrissakssak.feed.controller.response.DefaultIdResponse;
import com.supernova.ssagrissakssak.feed.controller.response.TokenResponse;
import com.supernova.ssagrissakssak.feed.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/auth/users")
    public ResultResponse<DefaultIdResponse> join(@Valid @RequestBody UserCreateRequest request) {
        var joined = userService.join(request.toUser());

        return new ResultResponse<>(DefaultIdResponse.of(joined));
    }

    @PutMapping("/auth/users/approve")
    public ResultResponse<Void> approve(@Valid @RequestBody ApproveRequest approveRequest) {
        userService.approve(approveRequest);
        return new ResultResponse<>();
    }

    @PostMapping("/auth/users/sign-in")
    public ResultResponse<TokenResponse> signIn(@Valid @RequestBody SignInRequest signInRequest) {
        return new ResultResponse<>(userService.signIn(signInRequest));
    }
}
