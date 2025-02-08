package com.tom.service.shortener.common;

import java.util.Random;

import org.springframework.stereotype.Service;

@Service
public class URLEncoding {

	private final String Alphabet = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
	private final Random random = new Random();
	
    public String generateShortKey() {
		StringBuilder shortKey = new StringBuilder();
		for (int i = 0; i < 10; i++) {
			shortKey.append(Alphabet.charAt(random.nextInt(Alphabet.length())));
		}
		return shortKey.toString();
	}

    public String generateURL() {
		return generateShortKey();
	}

}