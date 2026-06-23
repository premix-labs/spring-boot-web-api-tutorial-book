package com.example.backendapi.service;

import com.example.backendapi.common.PageResponse;
import com.example.backendapi.dto.ChangeRoleRequest;
import com.example.backendapi.dto.ChangeStatusRequest;
import com.example.backendapi.dto.UserResponse;
import com.example.backendapi.exception.UserNotFoundException;
import com.example.backendapi.model.Role;
import com.example.backendapi.model.User;
import com.example.backendapi.model.UserStatus;
import com.example.backendapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    public UserResponse changeRole(Long id, ChangeRoleRequest request) {
        User user = findUser(id);
        user.setRole(request.role());
        return toResponse(user);
    }

    @Transactional
    public UserResponse changeStatus(
            Long id,
            ChangeStatusRequest request,
            String actorEmail
    ) {
        User actor = userRepository.findByEmail(actorEmail)
                .orElseThrow(() -> new BadCredentialsException("Invalid token"));
        User target = findUser(id);

        if (actor.getId().equals(target.getId())
                && request.status() == UserStatus.INACTIVE) {
            throw new IllegalStateException("Admin cannot deactivate own account");
        }

        target.setStatus(request.status());
        return toResponse(target);
    }

    private User findUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
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
