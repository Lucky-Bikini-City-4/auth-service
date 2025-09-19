package com.dayaeyak.auth.domain.auth;

import com.dayaeyak.auth.common.properties.SocialProperties;
import com.dayaeyak.auth.common.util.JwtProvider;
import com.dayaeyak.auth.domain.auth.cache.redis.AuthRedisRepository;
import com.dayaeyak.auth.domain.auth.client.user.UserFeignClient;
import com.dayaeyak.auth.domain.auth.client.user.dto.request.UserSocialLoginRequestDto;
import com.dayaeyak.auth.domain.auth.client.user.dto.response.UserSocialLoginResponseDto;
import com.dayaeyak.auth.domain.auth.dto.request.AuthGoogleTokenRequestDto;
import com.dayaeyak.auth.domain.auth.dto.response.AuthGoogleTokenResponseDto;
import com.dayaeyak.auth.domain.auth.dto.response.AuthGoogleUserResponseDto;
import com.dayaeyak.auth.domain.auth.dto.response.AuthSocialLoginResponseDto;
import com.dayaeyak.auth.domain.auth.enums.ProviderType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@RequiredArgsConstructor
public class GoogleService {

    private final JwtProvider jwtProvider;
    private final RestTemplate restTemplate;
    private final SocialProperties socialProperties;
    private final AuthRedisRepository authRedisRepository;

    private final UserFeignClient userFeignClient;

    public String findLoginLink() {
        return socialProperties.google().uri().authorize() +
                "?client_id=" + socialProperties.google().client().id() +
                "&response_type=code" +
                "&scope=email%20profile" +
                "&access_type=offline" +
                "&redirect_uri=" + socialProperties.google().uri().redirect();
    }

    public AuthSocialLoginResponseDto loginGoogle(String code) {
        String providerAccessToken = findProviderAccessToken(code);
        AuthGoogleUserResponseDto userInfo = findProviderUserInfo(providerAccessToken);

        ProviderType providerType = ProviderType.GOOGLE;
        String providerId = userInfo.id();
        String email = userInfo.email();

        UserSocialLoginResponseDto user
                = userFeignClient.socialLogin(UserSocialLoginRequestDto.from(providerType, providerId, email));

        String accessToken = jwtProvider.generateAccessToken(user.userId(), user.role());
        String refreshToken = jwtProvider.generateRefreshToken(user.userId());

        authRedisRepository.saveRefreshToken(refreshToken, user.userId());

        return AuthSocialLoginResponseDto.from(accessToken, refreshToken);
    }

    private String findProviderAccessToken(String code) {
        AuthGoogleTokenRequestDto tokenRequest = AuthGoogleTokenRequestDto.builder()
                .clientId(socialProperties.google().client().id())
                .clientSecret(socialProperties.google().client().secret())
                .code(code)
                .redirectUri(socialProperties.google().uri().redirect())
                .grantType("authorization_code")
                .build();

        ResponseEntity<AuthGoogleTokenResponseDto> tokenResponse = restTemplate.postForEntity(socialProperties.google().uri().token(),
                tokenRequest, AuthGoogleTokenResponseDto.class);

        log.info("Google token response: {}", tokenResponse.getBody().toString());

        return tokenResponse.getBody().accessToken();
    }

    private AuthGoogleUserResponseDto findProviderUserInfo(String accessToken) {
        HttpHeaders userRequestHeaders = new HttpHeaders();
        userRequestHeaders.add("Authorization", "Bearer " + accessToken);

        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(userRequestHeaders);

        ResponseEntity<AuthGoogleUserResponseDto> result = restTemplate.exchange(
                socialProperties.google().uri().user(),
                HttpMethod.GET,
                httpEntity,
                AuthGoogleUserResponseDto.class
        );

        log.info("Google user response: {}", result.getBody().toString());

        return result.getBody();
    }
}
