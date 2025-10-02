package com.dayaeyak.auth.domain.auth.client.user.dto.request;

import com.dayaeyak.auth.domain.auth.dto.request.AuthSignupRequestDto;
import com.dayaeyak.auth.domain.auth.enums.UserRole;
import lombok.Builder;

@Builder
public record UserCreateRequestDto(
        String email,

        String password,

        String nickname,

        Integer age,

        String phone,

        UserRole role
) {

    public static UserCreateRequestDto from(AuthSignupRequestDto dto, String encodedPassword) {
        return UserCreateRequestDto.builder()
                .email(dto.email())
                .password(encodedPassword)
                .nickname(dto.nickname())
                .age(dto.age())
                .phone(dto.phone())
                .role(UserRole.of(dto.role()))
                .build();
    }
}
