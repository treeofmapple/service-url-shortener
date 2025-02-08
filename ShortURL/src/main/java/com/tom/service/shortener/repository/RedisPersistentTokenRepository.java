package com.tom.service.shortener.repository;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class RedisPersistentTokenRepository implements PersistentTokenRepository {

    private static final String REDIS_REMEMBER_ME_PREFIX = "remember-me:";

    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public void createNewToken(PersistentRememberMeToken token) {
        String key = REDIS_REMEMBER_ME_PREFIX + token.getSeries();
        redisTemplate.opsForValue().set(key, token, 30, TimeUnit.MINUTES);
    }

    @Override
    public void updateToken(String series, String tokenValue, Date lastUsed) {
        String key = REDIS_REMEMBER_ME_PREFIX + series;
        PersistentRememberMeToken existingToken = getTokenForSeries(series);
        if (existingToken != null) {
            PersistentRememberMeToken updatedToken =
                    new PersistentRememberMeToken(existingToken.getUsername(), series, tokenValue, lastUsed);
            redisTemplate.opsForValue().set(key, updatedToken, 30, TimeUnit.MINUTES);
        }
    }

    @Override
    public PersistentRememberMeToken getTokenForSeries(String seriesId) {
        String key = REDIS_REMEMBER_ME_PREFIX + seriesId;
        try {
            return (PersistentRememberMeToken) redisTemplate.opsForValue().get(key);
        } catch (DataAccessException e) {
            return null;
        }
    }

    @Override
    public void removeUserTokens(String username) {
        redisTemplate.keys(REDIS_REMEMBER_ME_PREFIX + "*").forEach(redisTemplate::delete);
    }
}