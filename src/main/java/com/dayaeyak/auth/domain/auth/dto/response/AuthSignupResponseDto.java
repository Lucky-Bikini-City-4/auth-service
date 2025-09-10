package com.dayaeyak.auth.domain.auth.dto.response;

public record AuthSignupResponseDto(
        String accessToken,

        String refreshToken
) {

    public static AuthSignupResponseDto from(String accessToken, String refreshToken) {
        return new AuthSignupResponseDto(accessToken, refreshToken);
    }
}
