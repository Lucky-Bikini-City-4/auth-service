package com.dayaeyak.auth.domain.auth;

import com.dayaeyak.auth.common.exception.CustomRuntimeException;
import com.dayaeyak.auth.common.exception.type.AuthExceptionType;
import com.dayaeyak.auth.common.util.JwtProvider;
import com.dayaeyak.auth.domain.auth.cache.redis.AuthRedisRepository;
import com.dayaeyak.auth.domain.auth.client.user.UserFeignClient;
import com.dayaeyak.auth.domain.auth.client.user.dto.request.UserCreateRequestDto;
import com.dayaeyak.auth.domain.auth.client.user.dto.request.UserFindByEmailRequestDto;
import com.dayaeyak.auth.domain.auth.client.user.dto.response.UserCreateResponseDto;
import com.dayaeyak.auth.domain.auth.client.user.dto.response.UserFindResponseDto;
import com.dayaeyak.auth.domain.auth.dto.request.AuthLoginRequestDto;
import com.dayaeyak.auth.domain.auth.dto.request.AuthReissueRequestDto;
import com.dayaeyak.auth.domain.auth.dto.request.AuthSignupRequestDto;
import com.dayaeyak.auth.domain.auth.dto.response.AuthLoginResponseDto;
import com.dayaeyak.auth.domain.auth.dto.response.AuthReissueResponseDto;
import com.dayaeyak.auth.domain.auth.dto.response.AuthSignupResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;
    private final AuthRedisRepository authRedisRepository;

    private final UserFeignClient userFeignClient;

    @Transactional
    public AuthSignupResponseDto signup(AuthSignupRequestDto dto) {
        // encode password
        String encodedPassword = passwordEncoder.encode(dto.password());

        UserCreateResponseDto user = userFeignClient.createUser(UserCreateRequestDto.from(dto, encodedPassword));

        // generate tokens
        String accessToken = jwtProvider.generateAccessToken(user.userId(), user.role());
        String refreshToken = jwtProvider.generateRefreshToken(user.userId());

        // save refresh token
        authRedisRepository.saveRefreshToken(refreshToken, user.userId());

        return AuthSignupResponseDto.from(accessToken, refreshToken);
    }

    public AuthLoginResponseDto login(AuthLoginRequestDto dto) {
        // Find user by email
        UserFindResponseDto user = userFeignClient.findUserByEmail(UserFindByEmailRequestDto.from(dto));

        // validate user password
        if (!passwordEncoder.matches(dto.password(), user.password())) {
            throw new CustomRuntimeException(AuthExceptionType.INVALID_CREDENTIALS);
        }

        // generate tokens
        String accessToken = jwtProvider.generateAccessToken(user.userId(), user.role());
        String refreshToken = jwtProvider.generateRefreshToken(user.userId());

        // save refresh token
        authRedisRepository.saveRefreshToken(refreshToken, user.userId());

        return AuthLoginResponseDto.from(accessToken, refreshToken);
    }

    public AuthReissueResponseDto reissueAccessToken(AuthReissueRequestDto dto) {
        Long userId = authRedisRepository.findAndDeleteByRefreshToken(dto.refreshToken())
                .orElseThrow(() -> new CustomRuntimeException(AuthExceptionType.INVALID_TOKEN));

        UserFindResponseDto user = userFeignClient.findUserById(userId);

        String accessToken = jwtProvider.generateAccessToken(user.userId(), user.role());
        String refreshToken = jwtProvider.generateRefreshToken(user.userId());

        authRedisRepository.saveRefreshToken(refreshToken, user.userId());

        return AuthReissueResponseDto.from(accessToken, refreshToken);
    }
}
