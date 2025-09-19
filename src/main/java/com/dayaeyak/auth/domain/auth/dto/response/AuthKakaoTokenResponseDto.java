package com.dayaeyak.auth.domain.auth.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AuthKakaoTokenResponseDto(
        @JsonProperty("token_type")
        String tokenType,

        @JsonProperty("access_token")
        String accessToken,

        @JsonProperty("id_token")
        String idToken,

        @JsonProperty("refresh_token")
        String refreshToken,

        @JsonProperty("expires_in")
        Integer expireIn,

        @JsonProperty("refresh_token_expires_in")
        Integer refreshTokenExpiresIn,

        String scope
) {
}
