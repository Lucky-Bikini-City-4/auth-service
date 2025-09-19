package com.dayaeyak.auth.domain.auth;

import com.dayaeyak.auth.common.util.JwtProvider;
import com.dayaeyak.auth.domain.auth.cache.redis.AuthRedisRepository;
import com.dayaeyak.auth.domain.auth.client.user.UserFeignClient;
import com.dayaeyak.auth.domain.auth.dto.response.AuthProviderUserInfoResponseDto;
import com.dayaeyak.auth.domain.auth.dto.response.AuthSocialLoginResponseDto;
import com.dayaeyak.auth.domain.auth.enums.ProviderType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
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

//        UserSocialLoginResponseDto user = userFeignClient.socialLogin(
//                UserSocialLoginRequestDto.from(
//                        providerType,
//                        userInfo.id(),
//                        userInfo.email()
//                )
//        );
//
//        String accessToken = jwtProvider.generateAccessToken(user.userId(), user.role());
//        String refreshToken = jwtProvider.generateRefreshToken(user.userId());
//
//        authRedisRepository.saveRefreshToken(refreshToken, user.userId());

        return AuthSocialLoginResponseDto.from(null, null);
    }

    private ProviderStrategy findProviderStrategy(ProviderType providerType) {
        return strategies.get(providerType);
    }
}
