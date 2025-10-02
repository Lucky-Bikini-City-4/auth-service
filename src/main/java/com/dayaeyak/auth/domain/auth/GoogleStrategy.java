package com.dayaeyak.auth.domain.auth;

import com.dayaeyak.auth.common.exception.CustomRuntimeException;
import com.dayaeyak.auth.common.exception.type.OAuthExceptionType;
import com.dayaeyak.auth.common.properties.SocialProperties;
import com.dayaeyak.auth.domain.auth.dto.request.AuthGoogleTokenRequestDto;
import com.dayaeyak.auth.domain.auth.dto.response.AuthGoogleTokenResponseDto;
import com.dayaeyak.auth.domain.auth.dto.response.AuthGoogleUserResponseDto;
import com.dayaeyak.auth.domain.auth.dto.response.AuthProviderUserInfoResponseDto;
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
public class GoogleStrategy implements ProviderStrategy {

    private final RestTemplate restTemplate;
    private final SocialProperties socialProperties;

    @Override
    public String findLoginPath() {
        return socialProperties.google().uri().authorize() +
                "?client_id=" + socialProperties.google().client().id() +
                "&response_type=code" +
                "&scope=email%20profile" +
                "&access_type=offline" +
                "&redirect_uri=" + socialProperties.google().uri().redirect();
    }

    @Override
    public String findAccessTokenFromProvider(String code) {
        AuthGoogleTokenRequestDto tokenRequest = AuthGoogleTokenRequestDto.builder()
                .clientId(socialProperties.google().client().id())
                .clientSecret(socialProperties.google().client().secret())
                .code(code)
                .redirectUri(socialProperties.google().uri().redirect())
                .grantType("authorization_code")
                .build();

        log.info("connect to GoogleServer for AccessToken");
        ResponseEntity<AuthGoogleTokenResponseDto> tokenResponse = restTemplate.postForEntity(socialProperties.google().uri().token(),
                tokenRequest, AuthGoogleTokenResponseDto.class);

        if (!tokenResponse.getStatusCode().is2xxSuccessful()
                || tokenResponse.getBody() == null
                || tokenResponse.getBody().accessToken() == null
        ) {
            throw new CustomRuntimeException(OAuthExceptionType.FAIL_TO_CALL_GOOGLE_SERVER);
        }

        return tokenResponse.getBody().accessToken();
    }

    @Override
    public AuthProviderUserInfoResponseDto findUserInfoFromProvider(String accessToken) {
        HttpHeaders userRequestHeaders = new HttpHeaders();
        userRequestHeaders.add("Authorization", "Bearer " + accessToken);

        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(userRequestHeaders);

        log.info("connect to GoogleServer for UserInfo");
        ResponseEntity<AuthGoogleUserResponseDto> userResponse = restTemplate.exchange(
                socialProperties.google().uri().user(),
                HttpMethod.GET,
                httpEntity,
                AuthGoogleUserResponseDto.class
        );

        if (!userResponse.getStatusCode().is2xxSuccessful() || userResponse.getBody() == null) {
            throw new CustomRuntimeException(OAuthExceptionType.FAIL_TO_CALL_GOOGLE_SERVER);
        }

        AuthGoogleUserResponseDto userInfo = userResponse.getBody();

        return AuthProviderUserInfoResponseDto.from(userInfo.id(), userInfo.email());
    }

    @Override
    public ProviderType getProviderType() {
        return ProviderType.GOOGLE;
    }
}
