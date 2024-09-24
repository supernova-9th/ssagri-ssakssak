package com.supernova.ssagrissakssak.feed.service;

import com.supernova.ssagrissakssak.core.exception.InvalidPasswordException;
import com.supernova.ssagrissakssak.core.exception.UserNotFoundException;
import com.supernova.ssagrissakssak.core.exception.UserRegistrationException;
import com.supernova.ssagrissakssak.core.security.JwtProvider;
import com.supernova.ssagrissakssak.feed.controller.request.ApproveRequest;
import com.supernova.ssagrissakssak.feed.controller.request.SignInRequest;
import com.supernova.ssagrissakssak.feed.controller.response.TokenResponse;
import com.supernova.ssagrissakssak.feed.persistence.repository.UserRepository;
import com.supernova.ssagrissakssak.feed.persistence.repository.entity.UserEntity;
import com.supernova.ssagrissakssak.feed.service.delegator.ApproveValidateDelegator;
import com.supernova.ssagrissakssak.feed.service.delegator.validator.SignInValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ApproveValidateDelegator approveValidateDelegator;
    private final SignInValidator signInValidator;
    private final JwtProvider jwtProvider;

    @Transactional
    public Long join(UserEntity user) {
        if(userRepository.existsByEmail(user.getEmail())) {
            throw new UserRegistrationException();
        }

        user.encodePassword(passwordEncoder.encode(user.getPassword()));

        return userRepository.save(user).getId();
    }

    /**
     * 사용자의 승인 요청을 처리하여 계정의 활성 상태를 변경합니다.
     * 1. 사용자를 조회
     * 2. 유효성 검증
     * 3. 활성 상태를 변경
     *
     * @param approveModel 승인 요청 모델.
     * @throws UserNotFoundException 지정된 email의 사용자를 찾을 수 없는 경우
     * @throws UserRegistrationException, InvalidPasswordException, InvalidVerificationCodeException
     */
    @Transactional
    public void approve(ApproveRequest approveModel) {
        UserEntity user = userRepository.findByEmail(approveModel.email()).orElseThrow(UserNotFoundException::new);
        approveValidateDelegator.validate(user, approveModel);
        user.changeActiveStatus();
    }

    /**
     * 사용자의 로그인 요청을 처리 후 JWT 토큰을 발급
     * 1. 사용자를 조회
     * 2. 유효성 검증
     * 3. 액세스 토큰, 리프레시 토큰 발급
     * 4. 리프레시 토큰은 사용자 엔티티에 저장
     *
     * @param signInRequest 로그인 요청 모델.
     *                      이 모델은 사용자의 이메일과 비밀번호를 포함합니다.
     * @return 새로운 액세스 토큰과 리프레시 토큰을 포함하는 `TokenResponse` 객체.
     * @throws UserNotFoundException 활성화된 계정이 존재하지 않을 경우
     * @throws InvalidPasswordException 잘못된 비밀번호인 경우
     */
    @Transactional
    public TokenResponse signIn(SignInRequest signInRequest) {
        UserEntity user = userRepository.findByEmailAndActiveStatus(signInRequest.email(), true).orElseThrow(UserNotFoundException::new);
        signInValidator.validate(user, signInRequest);
        TokenResponse token = jwtProvider.createTokenResponse(user.getEmail());
        user.registerRefreshToken(token.refreshToken());
        return token;
    }
}
