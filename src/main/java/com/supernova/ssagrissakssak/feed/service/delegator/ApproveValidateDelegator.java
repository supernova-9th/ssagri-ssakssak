package com.supernova.ssagrissakssak.feed.service.delegator;

import com.supernova.ssagrissakssak.feed.controller.request.ApproveRequestModel;
import com.supernova.ssagrissakssak.feed.persistence.repository.entity.UserEntity;
import com.supernova.ssagrissakssak.feed.service.delegator.validator.ApproveValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ApproveValidateDelegator {

    private final List<ApproveValidator> validators;

    public void validate(UserEntity user, ApproveRequestModel approveModel) {
        validators.stream().forEach(x -> x.validate(user, approveModel));
    }
}

