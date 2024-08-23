package com.supernova.ssagrissakssak.feed.service.delegator.validator;

import com.supernova.ssagrissakssak.core.exception.InvalidPasswordException;
import com.supernova.ssagrissakssak.feed.controller.request.AcceptRequestModel;
import com.supernova.ssagrissakssak.feed.persistence.repository.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PasswordValidator implements AcceptValidator {

    private final PasswordEncoder passwordEncoder;

    @Override
    public void validate(UserEntity user, AcceptRequestModel acceptModel) {
        if (!passwordEncoder.matches(acceptModel.password(), user.getPassword())) {
            throw new InvalidPasswordException();
        }
    }
}