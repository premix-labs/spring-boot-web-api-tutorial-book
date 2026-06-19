package com.example.secureadmin.service;

import com.example.secureadmin.dto.LoginRequest;
import com.example.secureadmin.dto.LoginResponse;
import com.example.secureadmin.dto.UserResponse;
import com.example.secureadmin.model.User;
import com.example.secureadmin.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthService.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public LoginResponse login(LoginRequest request) {
        log.info("Login attempt email={}", request.email());

        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> {
                    log.warn("Login failed email={} reason=user_not_found", request.email());
                    return new BadCredentialsException("Invalid email or password");
                });

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            log.warn("Login failed email={} reason=bad_password", request.email());
            throw new BadCredentialsException("Invalid email or password");
        }

        log.info("Login successful userId={}", user.getId());
        return new LoginResponse(jwtService.generateToken(user), toResponse(user));
    }

    private UserResponse toResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole(),
                user.getStatus(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }
}

