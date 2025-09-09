package com.dayaeyak.auth.domain.auth;

import com.dayaeyak.auth.common.constraints.AuthResponseMessage;
import com.dayaeyak.auth.common.entity.ApiResponse;
import com.dayaeyak.auth.domain.auth.dto.request.AuthLoginRequestDto;
import com.dayaeyak.auth.domain.auth.dto.request.AuthSignupRequestDto;
import com.dayaeyak.auth.domain.auth.dto.response.AuthLoginResponseDto;
import com.dayaeyak.auth.domain.auth.dto.response.AuthSignupResponseDto;
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

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<AuthSignupResponseDto>> signup(
            @RequestBody @Valid AuthSignupRequestDto authSignUpRequestDto
    ) {
        AuthSignupResponseDto data = authService.signup(authSignUpRequestDto);

        return ApiResponse.success(HttpStatus.CREATED, AuthResponseMessage.SIGNUP_SUCCESS, data);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthLoginResponseDto>> login(
            @RequestBody @Valid AuthLoginRequestDto authLoginRequestDto
    ) {
        AuthLoginResponseDto data = authService.login(authLoginRequestDto);

        return ApiResponse.success(HttpStatus.OK, AuthResponseMessage.LOGIN_SUCCESS, data);
    }
}
