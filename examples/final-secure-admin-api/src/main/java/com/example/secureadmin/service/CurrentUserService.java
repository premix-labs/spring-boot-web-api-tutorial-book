package com.example.secureadmin.service;

import com.example.secureadmin.exception.InactiveUserException;
import com.example.secureadmin.exception.UserNotFoundException;
import com.example.secureadmin.model.User;
import com.example.secureadmin.model.UserStatus;
import com.example.secureadmin.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CurrentUserService {

    private final UserRepository userRepository;

    public CurrentUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public User getRequiredActiveUser(Authentication authentication) {
        if (authentication == null || authentication.getName() == null) {
            throw new UserNotFoundException("Current user was not found");
        }

        User user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new UserNotFoundException("Current user was not found"));

        if (user.getStatus() != UserStatus.ACTIVE) {
            throw new InactiveUserException("User account is inactive");
        }

        return user;
    }
}
