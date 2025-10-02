package com.dayaeyak.auth.domain.auth.dto.request;

import lombok.Builder;

@Builder
public record AuthGoogleTokenRequestDto(
        String clientId,

        String redirectUri,

        String clientSecret,

        String responseType,

        String scope,

        String code,

        String accessType,

        String grantType,

        String state,

        String includeGrantedScopes,

        String loginHint,

        String prompt
) {
}
