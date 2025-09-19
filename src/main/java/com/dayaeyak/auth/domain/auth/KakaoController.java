package com.dayaeyak.auth.domain.auth;

import com.dayaeyak.auth.common.entity.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth/v1/kakao")
@RequiredArgsConstructor
public class KakaoController {

    private final KakaoService kakaoService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<String>> getLoginLink() {
        String data = kakaoService.findLoginLink();

        return ApiResponse.success(HttpStatus.OK, "", data);
    }

    @GetMapping("/login")
    public ResponseEntity<ApiResponse<Void>> login(
            @RequestParam String code
    ) {
        kakaoService.loginKakao(code);

        return ApiResponse.success(HttpStatus.OK, "");
    }
}
