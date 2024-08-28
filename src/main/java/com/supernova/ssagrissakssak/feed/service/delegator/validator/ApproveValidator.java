package com.supernova.ssagrissakssak.feed.service.delegator.validator;

import com.supernova.ssagrissakssak.feed.controller.request.ApproveRequest;
import com.supernova.ssagrissakssak.feed.persistence.repository.entity.UserEntity;

public interface ApproveValidator {

    /**
     * 사용자 승인 요청을 검증합니다.
     *
     * @param user 검증할 사용자 엔티티.
     * @param approveModel 승인 요청 모델.
     */
    void validate(UserEntity user, ApproveRequest approveModel);
}