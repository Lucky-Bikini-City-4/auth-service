package com.dayaeyak.auth.domain.auth.cache.redis;

import com.dayaeyak.auth.domain.auth.cache.redis.model.TempSocialUserInfo;

import javax.swing.text.html.Option;
import java.util.Optional;

public interface AuthRedisRepository {

    void saveRefreshToken(String refreshToken, Long userId);

    Optional<Long> findAndDeleteByRefreshToken(String refreshToken);

    boolean deleteByRefreshToken(String refreshToken);

    void saveSocialUserInfo(String tempToken, TempSocialUserInfo tempSocialUserInfo);

    Optional<TempSocialUserInfo> findAndDeleteSocialUserInfo(String tempToken);
}
