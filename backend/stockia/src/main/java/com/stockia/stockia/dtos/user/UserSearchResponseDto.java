package com.stockia.stockia.dtos.user;

import com.stockia.stockia.enums.AccountStatus;
import com.stockia.stockia.enums.Role;

import java.time.LocalDateTime;
import java.util.UUID;

public record UserSearchResponseDto(
        UUID id,
        String email,
        String name,
        Role role,
        AccountStatus accountStatus,
        boolean deleted,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
