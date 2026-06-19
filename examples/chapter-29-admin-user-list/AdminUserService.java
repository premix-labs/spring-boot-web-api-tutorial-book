package com.example.secureadmin.service;

import com.example.secureadmin.common.PageResponse;
import com.example.secureadmin.dto.UserResponse;
import com.example.secureadmin.model.Role;
import com.example.secureadmin.model.User;
import com.example.secureadmin.model.UserStatus;
import com.example.secureadmin.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminUserService {

    private final UserRepository userRepository;

    public PageResponse<UserResponse> findUsers(
            int page,
            int size,
            UserStatus status,
            Role role,
            String keyword
    ) {
        int safeSize = Math.min(size, 100);
        Pageable pageable = PageRequest.of(
                page,
                safeSize,
                Sort.by("createdAt").descending()
        );

        return PageResponse.from(
                userRepository.searchForAdmin(status, role, normalize(keyword), pageable)
                        .map(this::toResponse)
        );
    }

    private String normalize(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return "";
        }
        return keyword.trim();
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
