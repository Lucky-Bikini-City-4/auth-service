package com.dayaeyak.auth.domain.auth;

import com.dayaeyak.auth.common.properties.SocialProperties;
import com.dayaeyak.auth.common.util.JwtProvider;
import com.dayaeyak.auth.domain.auth.cache.redis.AuthRedisRepository;
import com.dayaeyak.auth.domain.auth.client.user.UserFeignClient;
import com.dayaeyak.auth.domain.auth.client.user.dto.request.UserSocialLoginRequestDto;
import com.dayaeyak.auth.domain.auth.client.user.dto.response.UserSocialLoginResponseDto;
import com.dayaeyak.auth.domain.auth.dto.request.AuthKakaoTokenRequestDto;
import com.dayaeyak.auth.domain.auth.dto.response.AuthKakaoTokenResponseDto;
import com.dayaeyak.auth.domain.auth.dto.response.AuthKakaoUserResponseDto;
import com.dayaeyak.auth.domain.auth.dto.response.AuthSocialLoginResponseDto;
import com.dayaeyak.auth.domain.auth.enums.ProviderType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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
public class KakaoService {

    private final JwtProvider jwtProvider;
    private final RestTemplate restTemplate;
    private final SocialProperties socialProperties;
    private final AuthRedisRepository authRedisRepository;

    private final UserFeignClient userFeignClient;

    public String findLoginLink() {
        return socialProperties.kakao().uri().authorize() +
                "?response_type=code" +
                "&client_id=" + socialProperties.kakao().client().id() +
                "&redirect_uri=" + socialProperties.kakao().uri().redirect();
    }

    public AuthSocialLoginResponseDto loginKakao(String code) {
        String providerAccessToken = findProviderAccessToken(code);
        AuthKakaoUserResponseDto userInfo = findProviderUserInfo(providerAccessToken);

        ProviderType providerType = ProviderType.KAKAO;
        String providerId = userInfo.id().toString();
        String email = userInfo.kakaoAccount().email();

        UserSocialLoginResponseDto user
                = userFeignClient.socialLogin(UserSocialLoginRequestDto.from(providerType, providerId, email));

        String accessToken = jwtProvider.generateAccessToken(user.userId(), user.role());
        String refreshToken = jwtProvider.generateRefreshToken(user.userId());

        authRedisRepository.saveRefreshToken(refreshToken, user.userId());

        return AuthSocialLoginResponseDto.from(accessToken, refreshToken);
    }

    private String findProviderAccessToken(String code) {
        HttpHeaders tokenRequestHeaders = new HttpHeaders();
        tokenRequestHeaders.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        AuthKakaoTokenRequestDto tokenRequest = AuthKakaoTokenRequestDto.builder()
                .grantType("authorization_code")
                .clientId(socialProperties.kakao().client().id())
                .redirectUri(socialProperties.kakao().uri().redirect())
                .code(code)
                .clientSecret(socialProperties.kakao().client().secret())
                .build();

        HttpEntity<MultiValueMap<String, String>> tokenHttpRequest = new HttpEntity<>(tokenRequest.toMultivaluedMap(), tokenRequestHeaders);

        ResponseEntity<AuthKakaoTokenResponseDto> tokenResponse = restTemplate.postForEntity(socialProperties.kakao().uri().token(),
                tokenHttpRequest, AuthKakaoTokenResponseDto.class);

        log.info("Kakao token response: {}", tokenResponse.getBody().toString());

        return tokenResponse.getBody().accessToken();
    }

    private AuthKakaoUserResponseDto findProviderUserInfo(String accessToken) {
        HttpHeaders userRequestHeaders = new HttpHeaders();
        userRequestHeaders.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
        userRequestHeaders.add("Authorization", "Bearer " + accessToken);

        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(userRequestHeaders);

        ResponseEntity<AuthKakaoUserResponseDto> userResponse = restTemplate.exchange(
                socialProperties.kakao().uri().user(),
                HttpMethod.GET,
                httpEntity,
                AuthKakaoUserResponseDto.class
        );

        log.info("Kakao user response: {}", userResponse.getBody().toString());

        return userResponse.getBody();
    }
}
