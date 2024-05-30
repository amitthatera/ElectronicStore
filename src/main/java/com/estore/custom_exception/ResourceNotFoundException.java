package com.estore.custom_exception;

public class ResourceNotFoundException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	String message;

	public ResourceNotFoundException() {
		super("RESOURCE NOT FOUND !!");
	}

	public ResourceNotFoundException(String message) {
		super(message);
	}
}
