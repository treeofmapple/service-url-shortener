package com.tom.service.shortener.model;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "urls")
public class URL {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private UUID id;
	
	@Column(name = "original_url", nullable = false)
	private String originalUrl;
	
	@Column(name = "short_url", nullable = false, unique = true)
	private String shortUrl;
	
	@Column(name = "date_created", nullable = false, updatable = false)
	private LocalDateTime dateCreated;
	
	@Column(name = "expiration_time")
	private LocalDateTime expirationTime;
	
	@Column(name = "last_access_time")
	private LocalDateTime lastAccessTime;
	
	@Column(name = "access_count", nullable = false)
	private int accessCount;
	
    @PrePersist
    private void prePersist() {
        this.dateCreated = LocalDateTime.now();
    }
}
