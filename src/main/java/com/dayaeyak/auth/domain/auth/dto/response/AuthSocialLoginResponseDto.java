package com.dayaeyak.auth.domain.auth.dto.response;

import com.dayaeyak.auth.domain.auth.enums.SocialLoginFlag;
import lombok.Builder;

@Builder
public record AuthSocialLoginResponseDto(
        SocialLoginFlag flag,

        String accessToken,

        String refreshToken,

        String tempToken
) {

    public static AuthSocialLoginResponseDto success(String accessToken, String refreshToken) {
        return AuthSocialLoginResponseDto.builder()
                .flag(SocialLoginFlag.SUCCESS)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public static AuthSocialLoginResponseDto joinRequired(String tempToken) {
        return AuthSocialLoginResponseDto.builder()
                .flag(SocialLoginFlag.JOIN_REQUIRED)
                .tempToken(tempToken)
                .build();
    }
}
