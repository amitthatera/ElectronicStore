package com.estore.custom_exception;


public class FileNotSupportedException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	String message;
	
	public FileNotSupportedException() {
		super("IMAGE NOT SUPPORTED !!");
	}
	
	public FileNotSupportedException(String message) {
		super(message);
	}

}
