package com.dayaeyak.auth.domain.auth.client.user.dto.request;

import com.dayaeyak.auth.domain.auth.cache.redis.model.TempSocialUserInfo;
import com.dayaeyak.auth.domain.auth.dto.request.AuthSocialSignupRequestDto;
import com.dayaeyak.auth.domain.auth.enums.ProviderType;
import com.dayaeyak.auth.domain.auth.enums.UserRole;
import lombok.Builder;

@Builder
public record UserSocialSignupRequestDto(
        String nickname,

        Integer age,

        String phone,

        UserRole role,

        ProviderType providerType,

        String providerId,

        String providerEmail
) {

    public static UserSocialSignupRequestDto from(AuthSocialSignupRequestDto dto, TempSocialUserInfo userInfo) {
        return UserSocialSignupRequestDto.builder()
                .nickname(dto.nickname())
                .age(dto.age())
                .phone(dto.phone())
                .role(dto.role())
                .providerType(userInfo.providerType())
                .providerId(userInfo.id())
                .providerEmail(userInfo.email())
                .build();
    }
}
