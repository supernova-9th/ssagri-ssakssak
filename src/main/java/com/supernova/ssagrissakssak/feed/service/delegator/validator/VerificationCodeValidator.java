package com.supernova.ssagrissakssak.feed.service.delegator.validator;

import com.supernova.ssagrissakssak.core.exception.InvalidVerificationCodeException;
import com.supernova.ssagrissakssak.feed.controller.request.ApproveRequest;
import com.supernova.ssagrissakssak.feed.persistence.repository.entity.UserEntity;
import org.springframework.stereotype.Service;

@Service
public class VerificationCodeValidator implements ApproveValidator {

    /**
     * 승인 요청에서 입력된 인증 코드가 사용자 계정의 인증 코드와 일치하는지 검증합니다.
     *
     * @param user 검증할 사용자 엔티티.
     * @param approveModel 승인 요청 모델.
     * @throws InvalidVerificationCodeException 입력된 인증 코드가 사용자 계정에 저장된 인증 코드와 일치하지 않을 경우
     */
    @Override
    public void validate(UserEntity user, ApproveRequest approveModel) {
        if (!user.getVerificationCode().equals(approveModel.authenticationCode())) {
            throw new InvalidVerificationCodeException();
        }
    }
}