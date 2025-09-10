package com.dayaeyak.auth.domain.auth.cache.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class AuthRedisRepositoryImpl implements AuthRedisRepository {

    @Value("${jwt.refresh.expiration}")
    private long refreshExpiration;

    private final RedisTemplate<String, Long> redisTemplate;

    @Override
    public void saveRefreshToken(String refreshToken, Long userId) {
        redisTemplate.opsForValue()
                .set(refreshToken, userId, Duration.ofMillis(refreshExpiration));
    }

    @Override
    public Optional<Long> findAndDeleteByRefreshToken(String refreshToken) {
        Long userId = redisTemplate.opsForValue()
                .getAndDelete(refreshToken);

        return Optional.ofNullable(userId);
    }
}
