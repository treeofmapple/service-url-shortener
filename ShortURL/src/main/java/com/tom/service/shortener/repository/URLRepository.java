package com.tom.service.shortener.repository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.tom.service.shortener.model.URL;

import jakarta.transaction.Transactional;

@Repository
public interface URLRepository extends JpaRepository<URL, UUID> {

    @Transactional
    @Modifying
    @Query("DELETE FROM URL u WHERE u.expirationTime <= :expirationTime")
    int deleteByExpirationTimeBefore(LocalDateTime expirationTime);
	
	Optional<URL> findByShortUrl(String shortUrl);
	
	Optional<URL> findByOriginalUrl(String originalUrl);
	
	boolean existsByShortUrl(String shortUrl);
	
	boolean existsByOriginalUrl(String originalUrl);
	
}
