package com.dayaeyak.auth.domain.auth.dto.response;

public record AuthLoginResponseDto(
        String accessToken,

        String refreshToken
) {
    public static AuthLoginResponseDto from(String accessToken, String refreshToken) {
        return new AuthLoginResponseDto(accessToken, refreshToken);
    }
}
