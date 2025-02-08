package com.tom.service.shortener.common;

import java.time.Duration;

public class DurationUtil {

	public static Duration parseDuration(String durationStr) {
		if (durationStr == null || durationStr.isEmpty()) {
			throw new IllegalArgumentException("Duration string cannot be null or empty.");
		}

		// Extract the unit (last character) and the number part of the string
		char unit = durationStr.charAt(durationStr.length() - 1);
		long value = Long.parseLong(durationStr.substring(0, durationStr.length() - 1));

		switch (unit) {
		case 'm':
			return Duration.ofMinutes(value);
		case 'h':
			return Duration.ofHours(value);
		default:
			throw new IllegalArgumentException("Unsupported time unit: " + unit);
		}
	}
}