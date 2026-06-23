package com.example.backendapi.service;

import com.example.backendapi.dto.LoginRequest;
import com.example.backendapi.dto.LoginResponse;
import com.example.backendapi.dto.RegisterRequest;
import com.example.backendapi.dto.UserResponse;
import com.example.backendapi.exception.DuplicateUserException;
import com.example.backendapi.exception.InactiveUserException;
import com.example.backendapi.exception.InvalidCredentialsException;
import com.example.backendapi.model.Role;
import com.example.backendapi.model.User;
import com.example.backendapi.model.UserStatus;
import com.example.backendapi.repository.UserRepository;
import java.util.Locale;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Transactional
    public UserResponse register(RegisterRequest request) {
        String username = request.username().trim();
        String email = normalizeEmail(request.email());

        if (userRepository.existsByUsername(username)) {
            throw new DuplicateUserException("Username already exists");
        }

        if (userRepository.existsByEmail(email)) {
            throw new DuplicateUserException("Email already exists");
        }

        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setRole(Role.USER);
        user.setStatus(UserStatus.ACTIVE);

        return UserResponse.from(userRepository.save(user));
    }

    @Transactional(readOnly = true)
    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(normalizeEmail(request.email()))
                .orElseThrow(() -> new InvalidCredentialsException("Email or password is incorrect"));

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new InvalidCredentialsException("Email or password is incorrect");
        }

        if (user.getStatus() != UserStatus.ACTIVE) {
            throw new InactiveUserException("User account is inactive");
        }

        return new LoginResponse(
                jwtService.generateToken(user),
                "Bearer",
                jwtService.expirationSeconds(),
                UserResponse.from(user)
        );
    }

    private String normalizeEmail(String email) {
        return email.trim().toLowerCase(Locale.ROOT);
    }
}
