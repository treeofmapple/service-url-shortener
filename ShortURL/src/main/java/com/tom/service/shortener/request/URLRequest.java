package com.tom.service.shortener.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record URLRequest(
		
		@NotNull(message = "URL não pode ser nula")
		@NotBlank(message = "URL não pode estar em branco")
		@Pattern(regexp = "^(https?|ftp)://.+$", message = "A URL deve ser válida (exemplo: http://www.exemplo.com)")
		@Schema(
				accessMode = Schema.AccessMode.READ_WRITE,
				allowableValues = "String",
				type = "string",
				description = "Inserir a URL no sistema",
				example = "https://www.youtube.com/")
		String url) {

}
