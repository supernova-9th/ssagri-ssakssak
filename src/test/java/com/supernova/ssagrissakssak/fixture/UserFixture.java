package com.supernova.ssagrissakssak.fixture;

import com.supernova.ssagrissakssak.feed.persistence.repository.entity.UserEntity;

public class UserFixture {

    public static UserEntity get() {
        return UserEntity.builder()
                .id(1L)
                .email("test@email.com")
                .password("encodedPassword")
                .activeStatus(false)
                .build();
    }

    public static UserEntity initUser() {
        return UserEntity.builder()
                .email("test@email.com")
                .password("encodedPassword")
                .activeStatus(false)
                .build();
    }

}
