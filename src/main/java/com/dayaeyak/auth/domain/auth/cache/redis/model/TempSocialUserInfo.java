package com.dayaeyak.auth.domain.auth.cache.redis.model;

import com.dayaeyak.auth.domain.auth.enums.ProviderType;

public record TempSocialUserInfo(
        ProviderType providerType,

        String id,

        String email
) {
}
