package com.dayaeyak.auth.domain.auth.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AuthKakaoUserResponseDto(
        Long id,

        @JsonProperty("kakao_account")
        AuthKakaoUserAccountResponseDto kakaoAccount
) {
}