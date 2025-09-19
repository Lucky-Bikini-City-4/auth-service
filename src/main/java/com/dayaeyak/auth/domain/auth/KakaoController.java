package com.dayaeyak.auth.domain.auth;

import com.dayaeyak.auth.common.constant.AuthResponseMessage;
import com.dayaeyak.auth.common.entity.ApiResponse;
import com.dayaeyak.auth.domain.auth.dto.response.AuthSocialLoginResponseDto;
import com.dayaeyak.auth.domain.auth.enums.ProviderType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth/login/kakao")
@RequiredArgsConstructor
public class KakaoController {

    private final OAuthService oAuthService;

    @PostMapping
    public ResponseEntity<ApiResponse<String>> getLoginLink() {
        String data = oAuthService.findLoginUrl(ProviderType.KAKAO);

        return ApiResponse.success(HttpStatus.OK, AuthResponseMessage.KAKAO_LOGIN_LINK, data);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<AuthSocialLoginResponseDto>> login(
            @RequestParam String code
    ) {
        AuthSocialLoginResponseDto data = oAuthService.processSocialLogin(ProviderType.KAKAO, code);

        return ApiResponse.success(HttpStatus.OK, AuthResponseMessage.KAKAO_LOGIN, data);
    }
}
