package com.dayaeyak.auth.domain.auth.cache.redis;

public interface AuthRedisRepository {

    void saveRefreshToken(String refreshToken, Long userId);
}
