package com.tom.service.shortener.exception;

import java.util.Map;

public record ErrorResponse(Map<String, String> errors) {
}
