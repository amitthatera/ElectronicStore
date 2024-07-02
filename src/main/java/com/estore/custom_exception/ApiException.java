package com.estore.custom_exception;


import java.io.Serial;

public class ApiException extends RuntimeException {

	@Serial
	private static final long serialVersionUID = 1L;
	
	String message;
	
	public ApiException() {
		super("Something Went Wrong !!");
	}
	
	public ApiException(String message) {
		super(message);
	}

}
