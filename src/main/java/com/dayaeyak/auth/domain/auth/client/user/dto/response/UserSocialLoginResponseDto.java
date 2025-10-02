package com.dayaeyak.auth.domain.auth.client.user.dto.response;

import com.dayaeyak.auth.domain.auth.enums.SocialLoginFlag;
import com.dayaeyak.auth.domain.auth.enums.UserRole;

public record UserSocialLoginResponseDto(
        SocialLoginFlag flag,

        Long userId,

        UserRole role
) {
}
