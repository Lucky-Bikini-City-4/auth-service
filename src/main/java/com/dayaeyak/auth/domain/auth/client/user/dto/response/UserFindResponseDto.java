package com.dayaeyak.auth.domain.auth.client.user.dto.response;

import com.dayaeyak.auth.domain.auth.enums.UserRole;

public record UserFindResponseDto(
        Long userId,

        String password,

        UserRole role
) {
}
