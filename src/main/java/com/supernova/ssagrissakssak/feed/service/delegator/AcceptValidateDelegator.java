package com.supernova.ssagrissakssak.feed.service.delegator;

import com.supernova.ssagrissakssak.feed.controller.request.AcceptRequestModel;
import com.supernova.ssagrissakssak.feed.persistence.repository.entity.UserEntity;
import com.supernova.ssagrissakssak.feed.service.delegator.validator.AcceptValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AcceptValidateDelegator {

    private final List<AcceptValidator> validators;

    public void validate(UserEntity user, AcceptRequestModel acceptModel) {
        validators.stream().forEach(x -> x.validate(user, acceptModel));
    }
}

