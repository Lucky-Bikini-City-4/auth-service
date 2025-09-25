package com.dayaeyak.auth.domain.auth;

import com.dayaeyak.auth.common.constant.AuthResponseMessage;
import com.dayaeyak.auth.common.entity.ApiResponse;
import com.dayaeyak.auth.domain.auth.dto.request.*;
import com.dayaeyak.auth.domain.auth.dto.response.AuthLoginResponseDto;
import com.dayaeyak.auth.domain.auth.dto.response.AuthReissueResponseDto;
import com.dayaeyak.auth.domain.auth.dto.response.AuthSignupResponseDto;
import com.dayaeyak.auth.domain.auth.dto.response.AuthSocialSignupResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final OAuthService oAuthService;

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<AuthSignupResponseDto>> signup(
            @RequestBody @Valid AuthSignupRequestDto authSignUpRequestDto
    ) {
        AuthSignupResponseDto data = authService.signup(authSignUpRequestDto);

        return ApiResponse.success(HttpStatus.CREATED, AuthResponseMessage.SIGNUP, data);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthLoginResponseDto>> login(
            @RequestBody @Valid AuthLoginRequestDto authLoginRequestDto
    ) {
        AuthLoginResponseDto data = authService.login(authLoginRequestDto);

        return ApiResponse.success(HttpStatus.OK, AuthResponseMessage.LOGIN, data);
    }

    @PostMapping("/reissue")
    public ResponseEntity<ApiResponse<AuthReissueResponseDto>> reissue(
            @RequestBody @Valid AuthReissueRequestDto authReissueRequestDto
    ) {
        AuthReissueResponseDto data = authService.reissueAccessToken(authReissueRequestDto);

        return ApiResponse.success(HttpStatus.OK, AuthResponseMessage.REISSUE, data);
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(
            @RequestBody @Valid AuthLogoutRequestDto authLogoutRequestDto
    ) {
        authService.logout(authLogoutRequestDto);

        return ApiResponse.success(HttpStatus.OK, AuthResponseMessage.LOGOUT);
    }

    @PostMapping("/signup/social")
    public ResponseEntity<ApiResponse<AuthSocialSignupResponseDto>> socialSignup(
            @RequestBody @Valid AuthSocialSignupRequestDto authSocialSignupRequestDto
    ) {
        AuthSocialSignupResponseDto data = oAuthService.processSocialSignup(authSocialSignupRequestDto);

        return ApiResponse.success(HttpStatus.CREATED, AuthResponseMessage.SOCIAL_SIGNUP, data);
    }
}
