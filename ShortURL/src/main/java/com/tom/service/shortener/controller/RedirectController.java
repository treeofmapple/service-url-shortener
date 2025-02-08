package com.tom.service.shortener.controller;

import java.net.URI;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tom.service.shortener.exception.ServiceUnavailableException;
import com.tom.service.shortener.request.URLShortRequest;
import com.tom.service.shortener.service.URLService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/")
@RequiredArgsConstructor
@Tag(name = "RedirectSystem", description = "Redirect the URL")
public class RedirectController {

	private final URLService service;
	
	@GetMapping("{request}")
	public ResponseEntity<Void> redirectURL(@PathVariable URLShortRequest request) {
        try {
            var originalURL = service.redirectURL(request);
            log.info("Successfully redirected user with request URL: {}", request.url());
            return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(originalURL)).build();
        } catch (ServiceUnavailableException e) {
            log.error("Error redirecting URL: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
