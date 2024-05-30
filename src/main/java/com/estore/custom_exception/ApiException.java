package com.estore.custom_exception;


public class ApiException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	String message;
	
	public ApiException() {
		super("Something Went Wrong !!");
	}
	
	public ApiException(String message) {
		super(message);
	}

}
