package com.dayaeyak.auth.common.config;

import com.dayaeyak.auth.domain.auth.client.user.UserFeignClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(
        basePackageClasses = {
                UserFeignClient.class,
        }
)
public class OpenFeignConfig {
}
