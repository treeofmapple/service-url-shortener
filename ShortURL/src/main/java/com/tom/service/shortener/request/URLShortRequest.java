package com.tom.service.shortener.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record URLShortRequest(
		
		@NotNull(message = "URL não pode ser nula")
		@NotBlank(message = "URL não pode estar em branco")
		String url) {

}
