package com.dayaeyak.auth.domain.auth;

import com.dayaeyak.auth.common.exception.CustomRuntimeException;
import com.dayaeyak.auth.common.exception.type.OAuthExceptionType;
import com.dayaeyak.auth.common.properties.SocialProperties;
import com.dayaeyak.auth.domain.auth.dto.request.AuthKakaoTokenRequestDto;
import com.dayaeyak.auth.domain.auth.dto.response.AuthKakaoTokenResponseDto;
import com.dayaeyak.auth.domain.auth.dto.response.AuthKakaoUserResponseDto;
import com.dayaeyak.auth.domain.auth.dto.response.AuthProviderUserInfoResponseDto;
import com.dayaeyak.auth.domain.auth.enums.ProviderType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class KakaoStrategy implements ProviderStrategy {

    private final RestTemplate restTemplate;
    private final SocialProperties socialProperties;

    @Override
    public String findLoginPath() {
        return socialProperties.kakao().uri().authorize() +
                "?response_type=code" +
                "&client_id=" + socialProperties.kakao().client().id() +
                "&redirect_uri=" + socialProperties.kakao().uri().redirect();
    }

    @Override
    public String findAccessTokenFromProvider(String code) {
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

        if (tokenResponse.getStatusCode().is2xxSuccessful()
                || tokenResponse.getBody() == null
                || tokenResponse.getBody().accessToken() == null
        ) {
            throw new CustomRuntimeException(OAuthExceptionType.FAIL_TO_CALL_KAKAO_SERVER);
        }

        return tokenResponse.getBody().accessToken();
    }

    @Override
    public AuthProviderUserInfoResponseDto findUserInfoFromProvider(String accessToken) {
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

        if (userResponse.getStatusCode().is2xxSuccessful() || userResponse.getBody() == null) {
            throw new CustomRuntimeException(OAuthExceptionType.FAIL_TO_CALL_KAKAO_SERVER);
        }

        AuthKakaoUserResponseDto userInfo = userResponse.getBody();

        return AuthProviderUserInfoResponseDto.from(userInfo.id().toString(), userInfo.kakaoAccount().email());
    }

    @Override
    public ProviderType getProviderType() {
        return ProviderType.KAKAO;
    }
}
