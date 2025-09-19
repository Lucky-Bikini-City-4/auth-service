package com.dayaeyak.auth.domain.auth;

import com.dayaeyak.auth.domain.auth.dto.response.AuthProviderUserInfoResponseDto;
import com.dayaeyak.auth.domain.auth.enums.ProviderType;

public interface ProviderStrategy {

    String findLoginLink();

    String findAccessTokenFromProvider(String code);

    AuthProviderUserInfoResponseDto findUserInfoFromProvider(String accessToken);

    ProviderType getProviderType();
}
