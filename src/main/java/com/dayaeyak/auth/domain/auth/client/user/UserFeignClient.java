package com.dayaeyak.auth.domain.auth.client.user;

import com.dayaeyak.auth.domain.auth.client.user.dto.request.UserCreateRequestDto;
import com.dayaeyak.auth.domain.auth.client.user.dto.request.UserFindByEmailRequestDto;
import com.dayaeyak.auth.domain.auth.client.user.dto.request.UserSocialLoginRequestDto;
import com.dayaeyak.auth.domain.auth.client.user.dto.request.UserSocialSignupRequestDto;
import com.dayaeyak.auth.domain.auth.client.user.dto.response.UserCreateResponseDto;
import com.dayaeyak.auth.domain.auth.client.user.dto.response.UserFindResponseDto;
import com.dayaeyak.auth.domain.auth.client.user.dto.response.UserSocialLoginResponseDto;
import com.dayaeyak.auth.domain.auth.client.user.dto.response.UserSocialSignupResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name = "user-service",
        path = "/internal/users",
        fallbackFactory = UserFeignClientFallbackFactory.class
)
public interface UserFeignClient {

    @PostMapping("/create-user")
    UserCreateResponseDto createUser(@RequestBody UserCreateRequestDto request);

    @PostMapping("/find-user")
    UserFindResponseDto findUserByEmail(@RequestBody UserFindByEmailRequestDto request);

    @GetMapping("/{userId}")
    UserFindResponseDto findUserById(@PathVariable Long userId);

    @PostMapping("/social-login")
    UserSocialLoginResponseDto socialLogin(@RequestBody UserSocialLoginRequestDto request);

    @PostMapping("/social-join")
    UserSocialSignupResponseDto socialSignup(@RequestBody UserSocialSignupRequestDto request);
}
