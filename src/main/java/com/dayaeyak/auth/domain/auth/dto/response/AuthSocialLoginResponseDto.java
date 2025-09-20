package com.dayaeyak.auth.domain.auth.dto.response;

import com.dayaeyak.auth.domain.auth.enums.ProviderType;
import com.dayaeyak.auth.domain.auth.enums.SocialLoginFlag;
import lombok.Builder;

@Builder
public record AuthSocialLoginResponseDto(
        SocialLoginFlag flag,

        ProviderType providerType,

        String accessToken,

        String refreshToken,

        String tempToken
) {

    public static AuthSocialLoginResponseDto success(ProviderType providerType, String accessToken, String refreshToken) {
        return AuthSocialLoginResponseDto.builder()
                .flag(SocialLoginFlag.SUCCESS)
                .providerType(providerType)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public static AuthSocialLoginResponseDto joinRequired(ProviderType providerType, String tempToken) {
        return AuthSocialLoginResponseDto.builder()
                .flag(SocialLoginFlag.JOIN_REQUIRED)
                .providerType(providerType)
                .tempToken(tempToken)
                .build();
    }
}
