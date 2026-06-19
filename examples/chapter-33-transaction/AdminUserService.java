package com.example.secureadmin.service;

import com.example.secureadmin.dto.ChangeStatusRequest;
import com.example.secureadmin.dto.UserResponse;
import com.example.secureadmin.exception.UserNotFoundException;
import com.example.secureadmin.model.User;
import com.example.secureadmin.model.UserStatus;
import com.example.secureadmin.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminUserService {

    private final UserRepository userRepository;
    private final AuditLogService auditLogService;

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
