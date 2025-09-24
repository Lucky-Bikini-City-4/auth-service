package com.dayaeyak.auth.domain.auth.dto.request;

import jakarta.ws.rs.core.MultivaluedHashMap;
import lombok.Builder;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Builder
public record AuthKakaoTokenRequestDto(
        String grantType,

        String clientId,

        String redirectUri,

        String code,

        String clientSecret
) {

    public MultiValueMap<String, String> toMultivaluedMap() {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("grant_type", grantType);
        map.add("client_id", clientId);
        map.add("redirect_uri", redirectUri);
        map.add("code", code);
        map.add("client_secret", clientSecret);

        return map;
    }
}
