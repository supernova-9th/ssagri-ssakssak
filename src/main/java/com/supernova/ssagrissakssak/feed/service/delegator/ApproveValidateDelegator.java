package com.supernova.ssagrissakssak.feed.service.delegator;

import com.supernova.ssagrissakssak.core.exception.UserRegistrationException;
import com.supernova.ssagrissakssak.feed.controller.request.ApproveRequest;
import com.supernova.ssagrissakssak.feed.persistence.repository.entity.UserEntity;
import com.supernova.ssagrissakssak.feed.service.delegator.validator.ApproveValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ApproveValidateDelegator {

    private final List<ApproveValidator> validators;

    /**
     * 모든 등록된 검증기(validator)를 사용하여 사용자 승인 요청을 검증합니다.
     *
     * @param user 검증할 사용자 엔티티.
     * @param approveModel 승인 요청 모델.
     * @throws UserRegistrationException, InvalidPasswordException, InvalidVerificationCodeException 등
     */
    public void validate(UserEntity user, ApproveRequest approveModel) {
        validators.stream().forEach(x -> x.validate(user, approveModel));
    }
}

