package com.dayaeyak.auth.domain.auth;

import com.dayaeyak.auth.common.properties.SocialProperties;
import com.dayaeyak.auth.domain.auth.dto.request.AuthGoogleTokenRequestDto;
import com.dayaeyak.auth.domain.auth.dto.response.AuthGoogleTokenResponseDto;
import com.dayaeyak.auth.domain.auth.dto.response.AuthGoogleUserResponseDto;
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

    private final RestTemplate restTemplate;
    private final SocialProperties socialProperties;

    public String findLoginLink() {
        return socialProperties.google().uri().authorize() +
                "?client_id=" + socialProperties.google().client().id() +
                "&response_type=code" +
                "&scope=email%20profile" +
                "&access_type=offline" +
                "&redirect_uri=" + socialProperties.google().uri().redirect();
    }

    public void loginGoogle(String code) {
        String accessToken = findProviderAccessToken(code);
        AuthGoogleUserResponseDto userInfo = findProviderUserInfo(accessToken);


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
