package com.tom.service.shortener.config;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.scheduling.annotation.Scheduled;

import com.tom.service.shortener.common.DurationUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class RedisConfig {

    @Value("${application.redis.default.time.shortUrl}")
    private String shortUrlTTL;

    @Value("${application.redis.default.time.userSessions}")
    private String userSessionsTTL;

    private final RedisConnectionFactory redisConnectionFactory;
    
    @Bean
    RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
        return template;
    }
    
    @Bean
    CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
        RedisCacheConfiguration defaultCacheConfig = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(5)) 
                .disableCachingNullValues();
        Map<String, RedisCacheConfiguration> cacheConfigurations = new HashMap<>();
        
        cacheConfigurations.put("shortUrls", RedisCacheConfiguration.defaultCacheConfig().entryTtl(DurationUtil.parseDuration(shortUrlTTL)));
        cacheConfigurations.put("userSessions", RedisCacheConfiguration.defaultCacheConfig().entryTtl(DurationUtil.parseDuration(userSessionsTTL)));

        return RedisCacheManager.builder(redisConnectionFactory)
                .cacheDefaults(defaultCacheConfig)
                .withInitialCacheConfigurations(cacheConfigurations)
                .build();
    }
    
    @Bean
    @Scheduled(fixedRateString = "${application.redis.sheduled.connection.time:300000}")
    String checkConnection() {
        try (RedisConnection connection = redisConnectionFactory.getConnection()) {
            return connection.ping();
        } catch (RedisConnectionFailureException e) {
        	log.error("Coulnd't connect on Redis Database: {}", LocalDateTime.now());
            return "Redis connection failed: " + e.getMessage();
        }
    }
}
