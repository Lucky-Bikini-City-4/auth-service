package com.dayaeyak.auth.domain.auth.client.user.dto.request;

import com.dayaeyak.auth.domain.auth.dto.request.AuthLoginRequestDto;

public record UserFindByEmailRequestDto(
        String email
) {

    public static UserFindByEmailRequestDto from(AuthLoginRequestDto dto) {
        return new UserFindByEmailRequestDto(dto.email());
    }
}
