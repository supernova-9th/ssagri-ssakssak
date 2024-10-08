package com.supernova.ssagrissakssak.feed.persistence.repository;

import com.supernova.ssagrissakssak.core.config.DefaultJpaRepository;
import com.supernova.ssagrissakssak.feed.persistence.repository.entity.UserEntity;

import java.util.Optional;

public interface UserRepository extends DefaultJpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByEmail(String email);

    Boolean existsByEmail(String email);

    Optional<UserEntity> findByEmailAndActiveStatus(String email, boolean isActive);

    Optional<UserEntity> findByEmailAndActiveStatusAndRefreshToken(String email, boolean activeStatus, String refreshToken);
}
