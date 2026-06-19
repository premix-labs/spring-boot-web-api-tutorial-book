package com.example.secureadmin.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import com.example.secureadmin.dto.LoginRequest;
import com.example.secureadmin.dto.RegisterRequest;
import com.example.secureadmin.exception.DuplicateUserException;
import com.example.secureadmin.model.User;
import com.example.secureadmin.repository.UserRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    UserRepository userRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    @Mock
    JwtService jwtService;

    @InjectMocks
    AuthService authService;

    @Test
    void register_shouldThrow_whenEmailAlreadyExists() {
        RegisterRequest request = new RegisterRequest(
                "john",
                "john@example.com",
                "password123"
        );

        when(userRepository.existsByEmail("john@example.com")).thenReturn(true);

        assertThrows(DuplicateUserException.class, () -> authService.register(request));
    }

    @Test
    void login_shouldReturnToken_whenPasswordMatches() {
        User user = new User();
        user.setId(1L);
        user.setUsername("john");
        user.setEmail("john@example.com");
        user.setPassword("hashed-password");

        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("password123", "hashed-password")).thenReturn(true);
        when(jwtService.generateToken(user)).thenReturn("jwt-token");

        var response = authService.login(new LoginRequest("john@example.com", "password123"));

        assertEquals("jwt-token", response.token());
    }

    @Test
    void login_shouldThrow_whenPasswordDoesNotMatch() {
        User user = new User();
        user.setEmail("john@example.com");
        user.setPassword("hashed-password");

        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrong", "hashed-password")).thenReturn(false);

        assertThrows(BadCredentialsException.class,
                () -> authService.login(new LoginRequest("john@example.com", "wrong")));
    }
}

