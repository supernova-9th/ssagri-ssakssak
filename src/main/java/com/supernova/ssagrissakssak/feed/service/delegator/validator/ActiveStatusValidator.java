package com.supernova.ssagrissakssak.feed.service.delegator.validator;

import com.supernova.ssagrissakssak.core.exception.UserRegistrationException;
import com.supernova.ssagrissakssak.feed.controller.request.ApproveRequest;
import com.supernova.ssagrissakssak.feed.persistence.repository.entity.UserEntity;
import org.springframework.stereotype.Service;

@Service
public class ActiveStatusValidator implements ApproveValidator {

    /**
     * 사용자의 활성 상태를 검증합니다.
     *
     * @param user 검증할 사용자 엔티티.
     * @param approveModel 승인 요청 모델.
     * @throws UserRegistrationException 사용자가 이미 활성화된 상태일 경우
     */
    @Override
    public void validate(UserEntity user, ApproveRequest approveModel) {
        if (user.isActive()) {
            throw new UserRegistrationException();
        }
    }
}

