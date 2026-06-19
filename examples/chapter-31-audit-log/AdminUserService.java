package com.example.secureadmin.service;

import com.example.secureadmin.common.PageResponse;
import com.example.secureadmin.dto.ChangeRoleRequest;
import com.example.secureadmin.dto.ChangeStatusRequest;
import com.example.secureadmin.dto.UserResponse;
import com.example.secureadmin.exception.UserNotFoundException;
import com.example.secureadmin.model.Role;
import com.example.secureadmin.model.User;
import com.example.secureadmin.model.UserStatus;
import com.example.secureadmin.repository.UserRepository;
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
    private final AuditLogService auditLogService;

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
    public UserResponse changeRole(
            Long id,
            ChangeRoleRequest request,
            String actorEmail
    ) {
        User actor = findActor(actorEmail);
        User target = findUser(id);
        Role oldRole = target.getRole();

        target.setRole(request.role());
        auditLogService.record(
                "CHANGE_ROLE",
                actor.getId(),
                target.getId(),
                oldRole.name(),
                request.role().name()
        );

        return toResponse(target);
    }

    @Transactional
    public UserResponse changeStatus(
            Long id,
            ChangeStatusRequest request,
            String actorEmail
    ) {
        User actor = findActor(actorEmail);
        User target = findUser(id);

        if (actor.getId().equals(target.getId())
                && request.status() == UserStatus.INACTIVE) {
            throw new IllegalStateException("Admin cannot deactivate own account");
        }

        UserStatus oldStatus = target.getStatus();
        target.setStatus(request.status());
        auditLogService.record(
                "CHANGE_STATUS",
                actor.getId(),
                target.getId(),
                oldStatus.name(),
                request.status().name()
        );

        return toResponse(target);
    }

    private User findActor(String actorEmail) {
        return userRepository.findByEmail(actorEmail)
                .orElseThrow(() -> new BadCredentialsException("Invalid token"));
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
