package com.dayaeyak.auth.domain.auth.cache.redis;

import java.util.Optional;

public interface AuthRedisRepository {

    void saveRefreshToken(String refreshToken, Long userId);

    Optional<Long> findAndDeleteByRefreshToken(String refreshToken);

    boolean deleteByRefreshToken(String refreshToken);
}
