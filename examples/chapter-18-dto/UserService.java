package com.example.secureadmin.service;

import com.example.secureadmin.dto.CreateUserRequest;
import com.example.secureadmin.dto.UpdateUserRequest;
import com.example.secureadmin.dto.UserResponse;
import com.example.secureadmin.exception.DuplicateUserException;
import com.example.secureadmin.exception.UserNotFoundException;
import com.example.secureadmin.model.User;
import com.example.secureadmin.repository.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public List<UserResponse> findAll() {
        return userRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    public UserResponse findById(Long id) {
        return toResponse(findEntityById(id));
    }

    public UserResponse create(CreateUserRequest request) {
        if (userRepository.existsByUsername(request.username())) {
            throw new DuplicateUserException("Username already exists");
        }

        if (userRepository.existsByEmail(request.email())) {
            throw new DuplicateUserException("Email already exists");
        }

        User user = new User();
        user.setUsername(request.username());
        user.setEmail(request.email());
        user.setPassword(request.password());

        return toResponse(userRepository.save(user));
    }

    public UserResponse update(Long id, UpdateUserRequest request) {
        User user = findEntityById(id);

        userRepository.findByUsername(request.username())
                .filter(existing -> !existing.getId().equals(id))
                .ifPresent(existing -> {
                    throw new DuplicateUserException("Username already exists");
                });

        userRepository.findByEmail(request.email())
                .filter(existing -> !existing.getId().equals(id))
                .ifPresent(existing -> {
                    throw new DuplicateUserException("Email already exists");
                });

        user.setUsername(request.username());
        user.setEmail(request.email());

        return toResponse(userRepository.save(user));
    }

    public void deleteById(Long id) {
        User user = findEntityById(id);
        userRepository.delete(user);
    }

    private User findEntityById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
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

