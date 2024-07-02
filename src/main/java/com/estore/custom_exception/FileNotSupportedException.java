package com.estore.custom_exception;


import java.io.Serial;

public class FileNotSupportedException extends RuntimeException {

	@Serial
	private static final long serialVersionUID = 1L;
	
	String message;
	
	public FileNotSupportedException() {
		super("IMAGE NOT SUPPORTED !!");
	}
	
	public FileNotSupportedException(String message) {
		super(message);
	}

}
