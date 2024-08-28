package com.supernova.ssagrissakssak.feed.controller;

import com.supernova.ssagrissakssak.core.exception.InvalidPasswordException;
import com.supernova.ssagrissakssak.core.exception.UserNotFoundException;
import com.supernova.ssagrissakssak.core.wrapper.ResultResponse;
import com.supernova.ssagrissakssak.feed.controller.request.ApproveRequest;
import com.supernova.ssagrissakssak.feed.controller.request.SignInRequest;
import com.supernova.ssagrissakssak.feed.controller.request.UserCreateRequest;
import com.supernova.ssagrissakssak.feed.controller.response.DefaultIdResponse;
import com.supernova.ssagrissakssak.feed.controller.response.TokenResponse;
import com.supernova.ssagrissakssak.feed.service.UserService;
import com.supernova.ssagrissakssak.feed.service.delegator.validator.ActiveStatusValidator;
import com.supernova.ssagrissakssak.feed.service.delegator.validator.VerificationCodeValidator;
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

    /**
     * 사용자 승인 요청을 처리합니다.
     *
     * @param approveRequest 승인 요청 정보를 담고 있는 객체.
     *        이 객체는 다음 필드를 포함해야 합니다:
     *        - email: 사용자의 이메일 주소 (필수)
     *        - password: 사용자의 비밀번호 (필수)
     *        - authenticationCode: 사용자의 인증 코드 (필수, 6자리, 숫자와 영문자로만 구성)
     * @return 처리 결과를 나타내는 응답 객체.
     * @throws UserNotFoundException 지정된 email의 사용자를 찾을 수 없는 경우
     * @throws InvalidPasswordException 잘못된 비밀번호인 경우
     * @throws VerificationCodeValidator 인증 코드가 올바르지 않은 경우
     * @throws ActiveStatusValidator 이미 활성화 상태인 계정인 경우
     */
    @PutMapping("/auth/users/approve")
    public ResultResponse<Void> approve(@Valid @RequestBody ApproveRequest approveRequest) {
        userService.approve(approveRequest);
        return new ResultResponse<>();
    }

    /**
     * 로그인 요청을 처리합니다.
     *
     * @param signInRequest 로그인 요청 정보를 담고 있는 객체.
     *        이 객체는 다음 필드를 포함해야 합니다:
     *        - email: 이메일 주소 (필수)
     *        - password: 비밀번호 (필수)
     * @return 액세스 토큰과 리프레시 토큰을 포함하는 응답 객체.
     * @throws UserNotFoundException 활성화된 계정이 존재하지 않을 경우
     * @throws InvalidPasswordException 잘못된 비밀번호인 경우
     */
    @PostMapping("/auth/users/sign-in")
    public ResultResponse<TokenResponse> signIn(@Valid @RequestBody SignInRequest signInRequest) {
        return new ResultResponse<>(userService.signIn(signInRequest));
    }
}
