package com.tom.service.shortener.controller;


import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tom.service.shortener.exception.ServiceUnavailableException;
import com.tom.service.shortener.request.URLComplete;
import com.tom.service.shortener.request.URLObject;
import com.tom.service.shortener.request.URLRequest;
import com.tom.service.shortener.request.URLResponse;
import com.tom.service.shortener.request.URLShortRequest;
import com.tom.service.shortener.service.URLService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Tag(name = "UrlSystem", description = "Operation of URL Shortening")
public class URLController {

	private final URLService service;
	
    @PostMapping("/shorten")
    public ResponseEntity<URLResponse> shortenURL(@RequestBody @Valid URLRequest request) {
        try {
            var redirectUrl = service.shortenURL(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(redirectUrl);
        } catch (ServiceUnavailableException e) {
            log.error("Error shortening URL: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
	
    @GetMapping("/basic")
    public ResponseEntity<URLResponse> findBasicURL(@RequestBody @Valid URLShortRequest request) {
        try {
            var urls = service.findBasicURL(request);
            log.info("Successfully retrieved ( Basic URL ) of: {}", request);
            return ResponseEntity.status(HttpStatus.OK).body(urls);
        } catch (ServiceUnavailableException e) {
            log.error("Error retrieving Basic URL: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/dev/get")
    public ResponseEntity<List<URLComplete>> findAllURL() {
        try {
            var urls = service.findAllURL();
            log.info("Successfully retrieved ( All URLs )");
            return ResponseEntity.status(HttpStatus.OK).body(urls);
        } catch (ServiceUnavailableException e) {
            log.error("Error retrieving all URLs: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/dev/object")
    public ResponseEntity<List<URLObject>> findObjectURL() {
        try {
            var urls = service.findObjectURL();
            log.info("Successfully retrieved ( ObjectURL )");
            return ResponseEntity.status(HttpStatus.OK).body(urls);
        } catch (ServiceUnavailableException e) {
            log.error("Error retrieving Object URL: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/dev/full")
    public ResponseEntity<URLComplete> findFullURL(@RequestBody @Valid URLShortRequest request) {
        try {
            var urls = service.findFullURL(request);
            log.info("Successfully retrieved ( Full URL ) of: {}", request);
            return ResponseEntity.status(HttpStatus.OK).body(urls);
        } catch (ServiceUnavailableException e) {
            log.error("Error retrieving Full URL: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/dev/delete")
    public ResponseEntity<Void> deleteURL(@RequestBody @Valid URLShortRequest request) {
        try {
            service.deleteURL(request);
            log.info("Successfully deleted URL: {}", request);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (ServiceUnavailableException e) {
            log.error("Error deleting URL: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
