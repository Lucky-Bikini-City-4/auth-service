package com.dayaeyak.auth.domain.auth.cache.redis;

import com.dayaeyak.auth.domain.auth.cache.redis.model.TempSocialUserInfo;
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

    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public void saveRefreshToken(String refreshToken, Long userId) {
        Duration ttl = Duration.ofMillis(refreshExpiration);

        redisTemplate.opsForValue()
                .set(refreshToken, userId, ttl);
    }

    @Override
    public Optional<Long> findAndDeleteByRefreshToken(String refreshToken) {
        Object data = redisTemplate.opsForValue()
                .getAndDelete(refreshToken);

        if (data instanceof Number number) {
            return Optional.of(number.longValue());
        }

        return Optional.empty();
    }

    @Override
    public boolean deleteByRefreshToken(String refreshToken) {
        return redisTemplate.delete(refreshToken);
    }

    @Override
    public void saveSocialUserInfo(String tempToken, TempSocialUserInfo tempSocialUserInfo) {
        Duration ttl = Duration.ofMinutes(30);

        redisTemplate.opsForValue()
                .set(tempToken, tempSocialUserInfo, ttl);
    }

    @Override
    public void deleteSocialUserInfo(String tempToken) {
        redisTemplate.delete(tempToken);
    }
}
