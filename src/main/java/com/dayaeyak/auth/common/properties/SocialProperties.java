package com.dayaeyak.auth.common.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "social")
public record SocialProperties(
        Google google,

        Kakao kakao
) {

    public record Google(
            Client client,

            Uri uri
    ) { }

    public record Kakao(
            Client client,

            Uri uri
    ) { }

    public record Client(
            String id,

            String secret
    ) { }

    public record Uri(
            String authorize,

            String redirect,

            String token,

            String user
    ) { }
}
