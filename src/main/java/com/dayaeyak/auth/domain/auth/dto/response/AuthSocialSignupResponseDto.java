package com.dayaeyak.auth.domain.auth.dto.response;

public record AuthSocialSignupResponseDto(
        String accessToken,

        String refreshToken
) {

    public static AuthSocialSignupResponseDto from(String accessToken, String refreshToken) {
        return new AuthSocialSignupResponseDto(accessToken, refreshToken);
    }
}
