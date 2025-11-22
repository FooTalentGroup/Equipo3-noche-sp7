package com.stockia.stockia.dtos.user;

import com.stockia.stockia.enums.AccountStatus;
import com.stockia.stockia.enums.Role;

public record UserSearchRequestDto(
        String email,
        String name,
        Role role,
        AccountStatus accountStatus
) {
}
