package com.tom.service.shortener.request;

import io.swagger.v3.oas.annotations.media.Schema;

public record URLObject(
		
		@Schema(
				accessMode = Schema.AccessMode.READ_ONLY, 
				type = "string", 
				description = "URL Encurtada", 
				example = "http://short.url/abc123")
		String originalURL
		
		) {

}
