package com.tom.service.shortener.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.tom.service.shortener.common.URLEncoding;
import com.tom.service.shortener.exception.DuplicateException;
import com.tom.service.shortener.exception.NotFoundException;
import com.tom.service.shortener.mapper.URLMapper;
import com.tom.service.shortener.model.URL;
import com.tom.service.shortener.repository.URLRepository;
import com.tom.service.shortener.request.URLComplete;
import com.tom.service.shortener.request.URLObject;
import com.tom.service.shortener.request.URLRequest;
import com.tom.service.shortener.request.URLResponse;
import com.tom.service.shortener.request.URLShortRequest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@EnableCaching
@RequiredArgsConstructor
public class URLService {

	private final URLRepository repository;
	private final URLMapper mapper;
	private final URLEncoding encoder;
	private final RedisTemplate<String, String> redisTemplate;
	private static final long EXPIRATION_TIME = 30;

	public URLResponse findBasicURL(URLShortRequest request) {
		log.info("User requesting ( Basic Url ) of: {}", request.url());
		var url = repository.findByShortUrl(request.url()).or(() -> repository.findByOriginalUrl(request.url()))
				.map(mapper::toUrlResponse).orElseThrow(() -> {
					log.warn("The requested Basic URL: {}, was not found", request);
					return new NotFoundException(String.format("Basic URL with info was not found %s", request.url()));
				});
		return url;
	}

	public URLResponse shortenURL(URLRequest request) {
		log.info("Shortening URL: {}", request.url());

		if (repository.existsByOriginalUrl(request.url())) {
			log.warn("URL already exists: {}", request);
			throw new DuplicateException(String.format("URL already exists: %s", request.url()));
		}

		String shortUrl;
		do {
			shortUrl = encoder.generateURL();
		} while (repository.existsByShortUrl(shortUrl));

		var urlEntity = mapper.buildAtributes(shortUrl, request.url(), LocalDateTime.now().plusDays(EXPIRATION_TIME));
		log.info("ShortURL generated: {}", shortUrl);

		repository.save(urlEntity);

		try {
			redisTemplate.opsForValue().set(shortUrl, request.url(), Duration.ofMinutes(5));
		} catch (RedisConnectionFailureException e) {
			log.warn("Failed to connect to Redis for URL: {}, cause: {}", request, e.getMessage());
		}

		var response = mapper.toUrlResponse(urlEntity);

		log.info("Successfully shortened URL: {} -> {}", request.url(), shortUrl);
		return response;
	}

	public String redirectURL(URLShortRequest request) {
	    log.info("Redirecting for short URL: {}", request.url());
	    String originalUrl = null;

	    if (isRedisAvailable()) {
	        try {
	            originalUrl = redisTemplate.opsForValue().get(request.url());

	            if (originalUrl != null) {
	                log.info("URL found in Redis cache: {}", originalUrl);
	            }
	        } catch (RedisConnectionFailureException e) {
	            log.warn("Failed to connect to Redis for URL: {}, cause: {}", request.url(), e.getMessage());
	        }
	    } else {
	        log.warn("Skipping Redis request; Redis is unavailable.");
	    }

	    if (originalUrl == null) {
	        URL urlFromDb = repository.findByShortUrl(request.url()).orElseThrow(() -> {
	            log.warn("The requested short URL: {}, was not found", request.url());
	            return new NotFoundException("Short URL not found");
	        });

	        originalUrl = urlFromDb.getOriginalUrl();

	        if (isRedisAvailable()) {
	            try {
	                redisTemplate.opsForValue().set(request.url(), originalUrl, Duration.ofMinutes(3));
	            } catch (RedisConnectionFailureException e) {
	                log.warn("Failed to connect to Redis for URL: {}, cause: {}", request.url(), e.getMessage());
	            }
	        } else {
	            log.warn("Skipping Redis request; Redis is unavailable.");
	        }
	    }
	    incrementAccessCountInRedis(request.url(), LocalDateTime.now());
	    return originalUrl;
	}

	public URLComplete findFullURL(URLShortRequest request) {
		log.info("User requesting the ( Full URL ) of: {}", request.url());
		var url = repository.findByShortUrl(request.url()).or(() -> repository.findByOriginalUrl(request.url()))
				.map(mapper::toUrlInfo).orElseThrow(() -> {
					log.warn("The requested Full URL: {}, was not found", request.url());
					return new NotFoundException(String.format("Full URL with info was not found %s", request.url()));
				});
		return url;
	}

	public List<URLComplete> findAllURL() {
		log.info("User is requesting to ( FindAllURL )");
		List<URL> urls = repository.findAll();
		if (urls.isEmpty()) {
			log.warn("Couldn't find any URL");
		}
		return urls.isEmpty() ? List.of() : urls.stream().map(mapper::toUrlInfo).collect(Collectors.toList());
	}

	public List<URLObject> findObjectURL() {
		log.info("User is requesting to ( FindObjectURL )");
		List<URL> urls = repository.findAll();
		if (urls.isEmpty()) {
			log.warn("Couldn't find any URL");
		}
		return urls.isEmpty() ? List.of() : urls.stream().map(mapper::toUrlObject).collect(Collectors.toList());
	}

	public void deleteURL(URLShortRequest request) {
		log.info("User is requesting to ( Delete Shorten URL ) :{}", request.url());

		var getUrl = repository.findByShortUrl(request.url()).or(() -> repository.findByOriginalUrl(request.url()))
				.orElseThrow(() -> {
					log.warn("Couldn't find the requested URL: {}", request);
					return new NotFoundException("URL not found");
				});

		if (isRedisAvailable()) {
			try {
				redisTemplate.delete(getUrl.getShortUrl());
				redisTemplate.delete(getUrl.getOriginalUrl());
			} catch (RedisConnectionFailureException e) {
				log.warn("Failed to connect to Redis for URL: {}, cause: {}", request, e.getMessage());
			}
		} else {
			log.warn("Skipping Redis cleanup; Redis is unavailable.");
		}
		repository.deleteById(getUrl.getId());
	}

	private void incrementAccessCountInRedis(String shortUrl, LocalDateTime time) {
		String аccessCount = "access_count:" + shortUrl;
		try {
			redisTemplate.opsForValue().increment(аccessCount);
			redisTemplate.opsForValue().set("last_access:" + shortUrl, LocalDateTime.now().toString(),
					Duration.ofMinutes(20));
			redisTemplate.expire(аccessCount, Duration.ofMinutes(20));
		} catch (RedisConnectionFailureException e) {
			log.warn("Failed to connect on Redis for URL: {}, cause: {}", shortUrl, e.getMessage());
		}
	}

	@Scheduled(fixedRateString = "${application.redis.sheduled.syncronization.time}")
	private void syncAccessCountsToDatabase() {
		log.info("Syncing access counts and last access times from Redis to database");

		if (!isRedisAvailable()) {
			log.warn("Skipping sync as Redis is unavailable");
			return;
		}

		try {
			Set<String> accessCountKeys = redisTemplate.keys("access_count:*");
			Set<String> lastAccessKeys = redisTemplate.keys("last_access:*");

			if (accessCountKeys != null) {
				for (String key : accessCountKeys) {
					String shortUrl = key.replace("access_count:", "");
					Integer accessCount = redisTemplate.opsForValue().get(key) != null
							? Integer.parseInt(redisTemplate.opsForValue().get(key))
							: 0;

					repository.findByShortUrl(shortUrl).ifPresent(url -> {
						url.setAccessCount(url.getAccessCount() + accessCount);
						repository.save(url);
						redisTemplate.delete(key);
					});
				}
			}

			if (lastAccessKeys != null) {
				for (String key : lastAccessKeys) {
					String shortUrl = key.replace("last_access:", "");
					String lastAccessTimeStr = redisTemplate.opsForValue().get(key);

					if (lastAccessTimeStr != null) {
						LocalDateTime lastAccessTime = LocalDateTime.parse(lastAccessTimeStr);

						repository.findByShortUrl(shortUrl).ifPresent(url -> {
							url.setLastAccessTime(lastAccessTime);
							repository.save(url);
							redisTemplate.delete(key);
						});
					}
				}
			}

		} catch (RedisConnectionFailureException e) {
			log.warn("Failed to connect to Redis during sync: {}", e.getMessage());
		}
	}

	@Scheduled(fixedRateString = "${application.database.expired.time}")
	private void cleanExpiredURL() {
		log.info("Starting cleanup for expired URL's");
		int deletedCount = repository.deleteByExpirationTimeBefore(LocalDateTime.now());
		log.info("Expired URLs cleanup complete. Total deleted: {}", deletedCount);
	}

	private boolean isRedisAvailable() {
		try {
			return redisTemplate.getConnectionFactory().getConnection().ping() != null;
		} catch (Exception e) {
			log.warn("Redis connection failed: {}", e.getMessage());
			return false;
		}
	}
}