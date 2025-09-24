package com.dayaeyak.auth.domain.auth.enums;

import com.dayaeyak.auth.common.exception.CustomRuntimeException;
import com.dayaeyak.auth.common.exception.type.AuthExceptionType;
import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.stream.Stream;

public enum ProviderType {
    GOOGLE,
    KAKAO,
    ;

    @JsonCreator
    public static ProviderType from(String social) {
        return Stream.of(values())
                .filter(v -> v.name().equalsIgnoreCase(social))
                .findFirst()
                .orElseThrow(() -> new CustomRuntimeException(AuthExceptionType.INVALID_SOCIAL_TYPE));
    }
}
