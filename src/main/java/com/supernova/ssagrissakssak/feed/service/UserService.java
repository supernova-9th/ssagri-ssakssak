package com.supernova.ssagrissakssak.feed.service;

import com.supernova.ssagrissakssak.core.exception.UserRegistrationException;
import com.supernova.ssagrissakssak.feed.persistence.repository.UserRepository;
import com.supernova.ssagrissakssak.feed.persistence.repository.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserEntity join(UserEntity user) {
       if(userRepository.existsByEmail(user.getEmail())) {
           throw new UserRegistrationException();
       }

       user.encodePassword(passwordEncoder.encode(user.getPassword()));

        return userRepository.save(user);
    }
}
