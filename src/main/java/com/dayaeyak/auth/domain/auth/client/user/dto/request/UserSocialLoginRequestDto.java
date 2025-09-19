package com.dayaeyak.auth.domain.auth.client.user.dto.request;

import com.dayaeyak.auth.domain.auth.enums.ProviderType;

public record UserSocialLoginRequestDto(
        ProviderType providerType,

        String providerId,

        String email
) {

    public static UserSocialLoginRequestDto from(ProviderType providerType, String providerId, String email) {
        return new UserSocialLoginRequestDto(providerType, providerId, email);
    }
}
