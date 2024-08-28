package com.supernova.ssagrissakssak.feed.service.delegator.validator;

import com.supernova.ssagrissakssak.core.exception.InvalidPasswordException;
import com.supernova.ssagrissakssak.feed.controller.request.ApproveRequest;
import com.supernova.ssagrissakssak.feed.controller.request.SignInRequest;
import com.supernova.ssagrissakssak.feed.persistence.repository.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PasswordValidator implements ApproveValidator, SignInValidator {

    private final PasswordEncoder passwordEncoder;

    /**
     * 승인 요청에서 입력된 비밀번호가 사용자 비밀번호와 일치하는지 검증합니다.
     *
     * @param user 검증할 사용자 엔티티.
     * @param approveModel 승인 요청 모델.
     * @throws InvalidPasswordException 입력된 비밀번호가 저장된 비밀번호와 일치하지 않을 경우
     */
    @Override
    public void validate(UserEntity user, ApproveRequest approveModel) {
        if (!passwordEncoder.matches(approveModel.password(), user.getPassword())) {
            throw new InvalidPasswordException();
        }
    }

    /**
     * 로그인 요청에서 입력된 비밀번호가 사용자 비밀번호와 일치하는지 검증합니다.
     *
     * @param user 검증할 사용자 엔티티.
     * @param signInRequest 로그인 요청 모델.
     * @throws InvalidPasswordException 입력된 비밀번호가 저장된 비밀번호와 일치하지 않을 경우
     */
    @Override
    public void validate(UserEntity user, SignInRequest signInRequest) {
        if (!passwordEncoder.matches(signInRequest.password(), user.getPassword())) {
            throw new InvalidPasswordException();
        }
    }
}