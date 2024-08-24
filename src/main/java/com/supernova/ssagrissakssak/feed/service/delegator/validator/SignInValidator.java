package com.supernova.ssagrissakssak.feed.service.delegator.validator;

import com.supernova.ssagrissakssak.feed.controller.request.SignInRequest;
import com.supernova.ssagrissakssak.feed.persistence.repository.entity.UserEntity;

public interface SignInValidator {
    void validate(UserEntity user, SignInRequest signInRequest);
}
