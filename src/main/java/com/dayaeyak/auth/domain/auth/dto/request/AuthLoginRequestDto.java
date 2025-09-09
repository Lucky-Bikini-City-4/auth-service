package com.dayaeyak.auth.domain.auth.dto.request;

import com.dayaeyak.auth.common.constraints.AuthValidationMessage;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record AuthLoginRequestDto(
        @NotBlank(message = AuthValidationMessage.INVALID_EMAIL_MESSAGE)
//        @Pattern(
//                regexp = AuthValidationMessage.EMAIL_REG_EXP,
//                message = AuthValidationMessage.INVALID_EMAIL_MESSAGE
//        )
        String email,

        @NotBlank(message = AuthValidationMessage.INVALID_PASSWORD_MESSAGE)
//        @Size(
//                min = AuthValidationMessage.PASSWORD_MIN_LENGTH,
//                max = AuthValidationMessage.PASSWORD_MAX_LENGTH,
//                message = AuthValidationMessage.INVALID_PASSWORD_MESSAGE
//        )
//        @Pattern(
//                regexp = AuthValidationMessage.PASSWORD_REG_EXP,
//                message = AuthValidationMessage.INVALID_PASSWORD_MESSAGE
//        )
        String password
) {
}
