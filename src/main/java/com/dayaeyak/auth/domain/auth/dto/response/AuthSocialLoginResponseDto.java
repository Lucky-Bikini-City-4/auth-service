package com.dayaeyak.auth.domain.auth.dto.response;

public record AuthSocialLoginResponseDto(
        String accessToken,

        String refreshToken
) {

    public static AuthSocialLoginResponseDto from(String accessToken, String refreshToken) {
        return new AuthSocialLoginResponseDto(accessToken, refreshToken);
    }
}
