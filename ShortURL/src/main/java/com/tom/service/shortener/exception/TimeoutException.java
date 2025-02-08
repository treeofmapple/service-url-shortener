package com.tom.service.shortener.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@SuppressWarnings("serial")
@EqualsAndHashCode(callSuper = true)
@Data
public class TimeoutException extends CustomGlobalException {

	private final String msg;
	
}
