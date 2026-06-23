package com.example.backendapi.service;

import com.example.backendapi.dto.CreateUserRequest;
import com.example.backendapi.dto.UpdateUserRequest;
import com.example.backendapi.model.User;
import com.example.backendapi.repository.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public User create(CreateUserRequest request) {
        if (userRepository.existsByUsername(request.username())) {
            throw new RuntimeException("Username already exists");
        }

        if (userRepository.existsByEmail(request.email())) {
            throw new RuntimeException("Email already exists");
        }

        User user = new User();
        user.setUsername(request.username());
        user.setEmail(request.email());
        user.setPassword(request.password());

        return userRepository.save(user);
    }

    public User update(Long id, UpdateUserRequest request) {
        User user = findById(id);

        userRepository.findByUsername(request.username())
                .filter(existing -> !existing.getId().equals(id))
                .ifPresent(existing -> {
                    throw new RuntimeException("Username already exists");
                });

        userRepository.findByEmail(request.email())
                .filter(existing -> !existing.getId().equals(id))
                .ifPresent(existing -> {
                    throw new RuntimeException("Email already exists");
                });

        user.setUsername(request.username());
        user.setEmail(request.email());

        return userRepository.save(user);
    }

    public void deleteById(Long id) {
        User user = findById(id);
        userRepository.delete(user);
    }
}

