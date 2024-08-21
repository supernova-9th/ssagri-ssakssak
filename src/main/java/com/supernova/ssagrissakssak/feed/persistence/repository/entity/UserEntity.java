package com.supernova.ssagrissakssak.feed.persistence.repository.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Getter
@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "user")
public class UserEntity {

    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "refresh_token", nullable = false)
    private String refreshToken;

    @Column(name = "active_status", nullable = false)
    private boolean activeStatus;

    @Column(name = "verification_code", nullable = false)
    private String verificationCode;
}
