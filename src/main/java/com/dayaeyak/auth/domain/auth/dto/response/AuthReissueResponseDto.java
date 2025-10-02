package com.dayaeyak.auth.domain.auth.dto.response;

public record AuthReissueResponseDto(
        String accessToken,

        String refreshToken
) {

    public static AuthReissueResponseDto from(String accessToken, String refreshToken) {
        return new AuthReissueResponseDto(accessToken, refreshToken);
    }
}
