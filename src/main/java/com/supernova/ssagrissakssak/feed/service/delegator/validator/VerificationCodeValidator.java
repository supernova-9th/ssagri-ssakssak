package com.supernova.ssagrissakssak.feed.service.delegator.validator;

import com.supernova.ssagrissakssak.core.exception.InvalidVerificationCodeException;
import com.supernova.ssagrissakssak.feed.controller.request.AcceptRequestModel;
import com.supernova.ssagrissakssak.feed.persistence.repository.entity.UserEntity;
import org.springframework.stereotype.Service;

@Service
public class VerificationCodeValidator implements AcceptValidator {
    @Override
    public void validate(UserEntity user, AcceptRequestModel acceptModel) {
        if (!user.getVerificationCode().equals(acceptModel.authenticationCode())) {
            throw new InvalidVerificationCodeException();
        }
    }
}