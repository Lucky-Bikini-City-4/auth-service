package com.dayaeyak.auth.domain.auth.client.user;

import com.dayaeyak.auth.common.exception.CustomInternalException;
import com.dayaeyak.auth.common.exception.CustomRuntimeException;
import com.dayaeyak.auth.common.exception.type.UserExceptionType;
import com.dayaeyak.auth.domain.auth.client.user.dto.request.UserCreateRequestDto;
import com.dayaeyak.auth.domain.auth.client.user.dto.request.UserFindByEmailRequestDto;
import com.dayaeyak.auth.domain.auth.client.user.dto.response.UserCreateResponseDto;
import com.dayaeyak.auth.domain.auth.client.user.dto.response.UserFindByEmailResponseDto;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class UserFeignClientFallbackFactory implements FallbackFactory<UserFeignClient> {

    @Override
    public UserFeignClient create(Throwable cause) {
        return new UserFeignClient() {
            @Override
            public UserCreateResponseDto createUser(UserCreateRequestDto request) {
                if (cause instanceof FeignException.Conflict e) {
                    String message = e.contentUTF8();
                    HttpStatus httpStatus = HttpStatus.valueOf(e.status());

                    throw new CustomInternalException(httpStatus, message);
                }

                throw new CustomRuntimeException(UserExceptionType.USER_SERVICE_UNAVAILABLE);
            }

            @Override
            public UserFindByEmailResponseDto findUserByEmail(UserFindByEmailRequestDto request) {
                if (cause instanceof FeignException.NotFound e) {
                    String message = e.contentUTF8();
                    HttpStatus httpStatus = HttpStatus.valueOf(e.status());

                    throw new CustomInternalException(httpStatus, message);
                }

                throw new CustomRuntimeException(UserExceptionType.USER_SERVICE_UNAVAILABLE);
            }
        };
    }
}
