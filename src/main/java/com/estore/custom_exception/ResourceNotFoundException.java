package com.estore.custom_exception;

import java.io.Serial;

public class ResourceNotFoundException extends RuntimeException {

	@Serial
	private static final long serialVersionUID = 1L;
	
	String message;

	public ResourceNotFoundException() {
		super("RESOURCE NOT FOUND !!");
	}

	public ResourceNotFoundException(String message) {
		super(message);
	}
}
