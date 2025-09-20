package com.dayaeyak.auth.domain.auth.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.stream.Stream;

public enum SocialLoginFlag {

    SUCCESS,
    JOIN_REQUIRED,
    ;

    @JsonCreator
    public static SocialLoginFlag of(String value) {
        return Stream.of(values())
                .filter(v -> v.name().equalsIgnoreCase(value))
                .findFirst()
                .orElse(null);
    }
}
