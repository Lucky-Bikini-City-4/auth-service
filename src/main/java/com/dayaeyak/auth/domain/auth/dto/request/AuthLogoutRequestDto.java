package com.dayaeyak.auth.domain.auth.dto.request;

import com.dayaeyak.auth.common.constant.AuthValidationMessage;
import jakarta.validation.constraints.NotBlank;

public record AuthLogoutRequestDto(
        @NotBlank(message = AuthValidationMessage.INVALID_REFRESH_TOKEN_MESSAGE)
        String refreshToken
) {
}
