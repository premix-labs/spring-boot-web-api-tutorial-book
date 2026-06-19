package com.example.secureadmin.service;

import com.example.secureadmin.dto.UserResponse;
import com.example.secureadmin.exception.SelfActionNotAllowedException;
import com.example.secureadmin.exception.UserNotFoundException;
import com.example.secureadmin.model.Role;
import com.example.secureadmin.model.User;
import com.example.secureadmin.model.UserStatus;
import com.example.secureadmin.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AdminUserService {

    private final UserRepository userRepository;
    private final AuditLogService auditLogService;

    public AdminUserService(UserRepository userRepository, AuditLogService auditLogService) {
        this.userRepository = userRepository;
        this.auditLogService = auditLogService;
    }

    @Transactional(readOnly = true)
    public Page<UserResponse> search(String keyword, Role role, UserStatus status, Pageable pageable) {
        String normalizedKeyword = keyword == null || keyword.isBlank()
                ? ""
                : keyword.trim();

        return userRepository.search(normalizedKeyword, role, status, pageable)
                .map(UserResponse::from);
    }

    @Transactional(readOnly = true)
    public UserResponse findById(Long id) {
        return UserResponse.from(findUser(id));
    }

    @Transactional
    public UserResponse changeRole(Long id, Role newRole, User actor) {
        User targetUser = findUser(id);

        if (actor.getId().equals(targetUser.getId()) && newRole != Role.ADMIN) {
            throw new SelfActionNotAllowedException("Admin cannot remove their own admin role");
        }

        Role oldRole = targetUser.getRole();
        targetUser.setRole(newRole);
        User savedUser = userRepository.save(targetUser);

        auditLogService.record(
                "CHANGE_ROLE",
                actor,
                savedUser,
                oldRole.name(),
                newRole.name()
        );

        return UserResponse.from(savedUser);
    }

    @Transactional
    public UserResponse changeStatus(Long id, UserStatus newStatus, User actor) {
        User targetUser = findUser(id);

        if (actor.getId().equals(targetUser.getId()) && newStatus != UserStatus.ACTIVE) {
            throw new SelfActionNotAllowedException("Admin cannot deactivate their own account");
        }

        UserStatus oldStatus = targetUser.getStatus();
        targetUser.setStatus(newStatus);
        User savedUser = userRepository.save(targetUser);

        auditLogService.record(
                "CHANGE_STATUS",
                actor,
                savedUser,
                oldStatus.name(),
                newStatus.name()
        );

        return UserResponse.from(savedUser);
    }

    private User findUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User was not found"));
    }
}
