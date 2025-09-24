package com.dayaeyak.auth.domain.auth;

import com.dayaeyak.auth.common.util.JwtProvider;
import com.dayaeyak.auth.domain.auth.cache.redis.AuthRedisRepository;
import com.dayaeyak.auth.domain.auth.cache.redis.model.TempSocialUserInfo;
import com.dayaeyak.auth.domain.auth.client.user.UserFeignClient;
import com.dayaeyak.auth.domain.auth.client.user.dto.request.UserSocialLoginRequestDto;
import com.dayaeyak.auth.domain.auth.client.user.dto.response.UserSocialLoginResponseDto;
import com.dayaeyak.auth.domain.auth.dto.response.AuthProviderUserInfoResponseDto;
import com.dayaeyak.auth.domain.auth.dto.response.AuthSocialLoginResponseDto;
import com.dayaeyak.auth.domain.auth.enums.ProviderType;
import com.dayaeyak.auth.domain.auth.enums.SocialLoginFlag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
public class OAuthService {

    private final JwtProvider jwtProvider;
    private final Map<ProviderType, ProviderStrategy> strategies;

    private final UserFeignClient userFeignClient;
    private final AuthRedisRepository authRedisRepository;

    public OAuthService(
            JwtProvider jwtProvider,
            List<ProviderStrategy> providerStrategies,
            UserFeignClient userFeignClient,
            AuthRedisRepository authRedisRepository
    ) {
        this.jwtProvider = jwtProvider;
        this.strategies = providerStrategies.stream()
                .collect(Collectors.toMap(
                        ProviderStrategy::getProviderType,
                        service -> service
                ));
        this.userFeignClient = userFeignClient;
        this.authRedisRepository = authRedisRepository;
    }

    public String findLoginUrl(ProviderType providerType) {
        return findProviderStrategy(providerType).findLoginLink();
    }

    public AuthSocialLoginResponseDto processSocialLogin(ProviderType providerType, String code) {
        ProviderStrategy strategy = findProviderStrategy(providerType);

        String providerAccessToken = strategy.findAccessTokenFromProvider(code);

        log.info("{} AccessToken: {}", providerType, providerAccessToken);

        AuthProviderUserInfoResponseDto userInfo = strategy.findUserInfoFromProvider(providerAccessToken);

        log.info("{} UserInfo: {}", providerType, userInfo);

        UserSocialLoginResponseDto userResponse = userFeignClient.socialLogin(
                UserSocialLoginRequestDto.from(
                        providerType,
                        userInfo.id(),
                        userInfo.email()
                )
        );

        // 추가 정보 필요 -> 소셜 회원 가입
        if (userResponse.flag().equals(SocialLoginFlag.JOIN_REQUIRED)) {
            String tempToken = UUID.randomUUID().toString().replace("-", "");

            authRedisRepository.saveSocialUserInfo(
                    tempToken,
                    new TempSocialUserInfo(providerType, userInfo.id(), userInfo.email())
            );

            return AuthSocialLoginResponseDto.joinRequired(providerType, tempToken);
        }

        String accessToken = jwtProvider.generateAccessToken(userResponse.userId(), userResponse.role());
        String refreshToken = jwtProvider.generateRefreshToken(userResponse.userId());

        authRedisRepository.saveRefreshToken(refreshToken, userResponse.userId());

        return AuthSocialLoginResponseDto.success(providerType, accessToken, refreshToken);
    }

    private ProviderStrategy findProviderStrategy(ProviderType providerType) {
        return strategies.get(providerType);
    }
}
