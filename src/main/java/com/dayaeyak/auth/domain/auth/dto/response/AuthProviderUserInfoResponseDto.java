package com.dayaeyak.auth.domain.auth.dto.response;

public record AuthProviderUserInfoResponseDto(
        String id,

        String email
) {

    public static AuthProviderUserInfoResponseDto from(String id, String email) {
        return new AuthProviderUserInfoResponseDto(id, email);
    }
}
