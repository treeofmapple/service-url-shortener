package com.tom.service.shortener.request;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;

public record URLResponse(
		
		@Schema(
				accessMode = Schema.AccessMode.READ_ONLY, 
				type = "string", 
				description = "URL Encurtada", 
				example = "http://short.url/abc123")
		String shortUrl,
		
		@Schema(
				accessMode = Schema.AccessMode.READ_ONLY, 
				type = "string", 
				description = "URL original", 
				example = "http://short.url/abc123")
		String originalUrl,
		
		@Schema(
				accessMode = Schema.AccessMode.READ_ONLY, 
				type = "string", 
				description = "Data de Criação", 
				format = "date-time", 
				example = "2025-01-27T12:00:00")
		LocalDateTime dataCriacao,

		@Schema(
				accessMode = Schema.AccessMode.READ_ONLY, 
				type = "string", 
				description = "Data de Expiração", 
				format = "date-time", 
				example = "2025-01-27T12:00:00")
		LocalDateTime dataExpiracao,
		
		@Schema(
				accessMode = Schema.AccessMode.READ_ONLY, 
				type = "int", 
				description = "Data de Expiração", 
				format = "int", 
				example = "4")
		int accessCount
		
		) {

}
