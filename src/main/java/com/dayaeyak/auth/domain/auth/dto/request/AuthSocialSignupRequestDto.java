package com.dayaeyak.auth.domain.auth.dto.request;

import com.dayaeyak.auth.common.constant.AuthValidationMessage;
import com.dayaeyak.auth.domain.auth.enums.UserRole;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AuthSocialSignupRequestDto(
        @NotBlank
        String tempToken,

        @NotBlank(message = AuthValidationMessage.INVALID_NICKNAME_MESSAGE)
//        @Size(
//                min = AuthValidationMessage.NICKNAME_MIN_LENGTH,
//                max = AuthValidationMessage.NICKNAME_MAX_LENGTH,
//                message = AuthValidationMessage.INVALID_NICKNAME_MESSAGE
//        )
        String nickname,

        @NotNull(message = AuthValidationMessage.INVALID_AGE_MESSAGE)
        Integer age,

        @NotBlank(message = AuthValidationMessage.INVALID_PHONE_MESSAGE)
//        @Pattern(
//                regexp = AuthValidationMessage.PHONE_REG_EXP,
//                message = AuthValidationMessage.INVALID_PHONE_MESSAGE
//        )
        String phone,

        @NotBlank(message = AuthValidationMessage.INVALID_ROLE_MESSAGE)
        UserRole role
) {
}
