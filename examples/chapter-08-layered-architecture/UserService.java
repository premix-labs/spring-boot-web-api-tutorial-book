package com.example.backendapi.service;

import com.example.backendapi.model.User;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final List<User> users = new ArrayList<>();
    private long nextId = 1L;

    public List<User> findAll() {
        return Collections.unmodifiableList(users);
    }

    public Optional<User> findById(Long id) {
        return users.stream()
                .filter(user -> user.getId().equals(id))
                .findFirst();
    }

    public User create(User request) {
        User user = new User(nextId++, request.getUsername(), request.getEmail());
        users.add(user);
        return user;
    }

    public boolean deleteById(Long id) {
        return users.removeIf(user -> user.getId().equals(id));
    }
}

