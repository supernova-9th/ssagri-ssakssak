package com.supernova.ssagrissakssak.feed.service.delegator.validator;

import com.supernova.ssagrissakssak.core.exception.UserRegistrationException;
import com.supernova.ssagrissakssak.feed.controller.request.AcceptRequestModel;
import com.supernova.ssagrissakssak.feed.persistence.repository.entity.UserEntity;
import org.springframework.stereotype.Service;

@Service
public class ActiveStatusValidator implements AcceptValidator {
    @Override
    public void validate(UserEntity user, AcceptRequestModel acceptModel) {
        if (user.isActive()) {
            throw new UserRegistrationException();
        }
    }
}

