package com.supernova.ssagrissakssak.feed.service.delegator.validator;

import com.supernova.ssagrissakssak.core.exception.UserRegistrationException;
import com.supernova.ssagrissakssak.feed.controller.request.ApproveRequestModel;
import com.supernova.ssagrissakssak.feed.persistence.repository.entity.UserEntity;
import org.springframework.stereotype.Service;

@Service
public class ActiveStatusValidator implements ApproveValidator {
    @Override
    public void validate(UserEntity user, ApproveRequestModel approveModel) {
        if (user.isActive()) {
            throw new UserRegistrationException();
        }
    }
}

