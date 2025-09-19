package com.dayaeyak.auth.domain.auth;

import com.dayaeyak.auth.common.constant.AuthResponseMessage;
import com.dayaeyak.auth.common.entity.ApiResponse;
import com.dayaeyak.auth.domain.auth.dto.response.AuthSocialLoginResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth/v1/google")
@RequiredArgsConstructor
public class GoogleController {

    private final GoogleService googleService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<String>> getLoginLink() {
        String data = googleService.findLoginLink();

        return ApiResponse.success(HttpStatus.OK, AuthResponseMessage.GOOGLE_LOGIN_LINK, data);
    }

    @GetMapping("/login")
    public ResponseEntity<ApiResponse<AuthSocialLoginResponseDto>> login(
            @RequestParam String code
    ) {
        AuthSocialLoginResponseDto data = googleService.loginGoogle(code);

        return ApiResponse.success(HttpStatus.OK, AuthResponseMessage.GOOGLE_LOGIN, data);
    }
}
