package com.supernova.ssagrissakssak.feed.service.delegator.validator;

import com.supernova.ssagrissakssak.core.exception.InvalidPasswordException;
import com.supernova.ssagrissakssak.feed.controller.request.ApproveRequest;
import com.supernova.ssagrissakssak.feed.controller.request.SignInRequest;
import com.supernova.ssagrissakssak.feed.persistence.repository.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PasswordValidator implements ApproveValidator, SignInValidator {

    private final PasswordEncoder passwordEncoder;

    @Override
    public void validate(UserEntity user, ApproveRequest approveModel) {
        if (!passwordEncoder.matches(approveModel.password(), user.getPassword())) {
            throw new InvalidPasswordException();
        }
    }

    @Override
    public void validate(UserEntity user, SignInRequest signInRequest) {
        if (!passwordEncoder.matches(signInRequest.password(), user.getPassword())) {
            throw new InvalidPasswordException();
        }
    }
}