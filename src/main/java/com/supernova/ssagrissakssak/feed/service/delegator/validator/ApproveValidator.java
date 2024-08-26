package com.supernova.ssagrissakssak.feed.service.delegator.validator;

import com.supernova.ssagrissakssak.feed.controller.request.ApproveRequest;
import com.supernova.ssagrissakssak.feed.persistence.repository.entity.UserEntity;

public interface ApproveValidator {
    void validate(UserEntity user, ApproveRequest approveModel);
}