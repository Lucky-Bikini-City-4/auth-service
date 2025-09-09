package com.dayaeyak.auth.domain.auth.client.user.dto.response;

import com.dayaeyak.auth.domain.auth.enums.UserRole;

public record UserCreateResponseDto(
        Long userId,

        UserRole role
) {
}
