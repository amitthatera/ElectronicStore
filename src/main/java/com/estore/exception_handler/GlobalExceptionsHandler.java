package com.estore.exception_handler;

import java.util.HashMap;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import javax.validation.ConstraintViolationException;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import com.estore.custom_exception.ApiException;
import com.estore.custom_exception.FileNotSupportedException;
import com.estore.custom_exception.ResourceNotFoundException;
import com.estore.utility.ApiResponses;

import io.jsonwebtoken.ExpiredJwtException;


@RestControllerAdvice
public class GlobalExceptionsHandler {

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ApiResponses> handleResourceNotFoundException(ResourceNotFoundException exception){
		String message = exception.getMessage();
		return new ResponseEntity<ApiResponses>(new ApiResponses(message, HttpStatus.NOT_FOUND), HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(ApiException.class)
	public ResponseEntity<ApiResponses> handleApiException(ApiException exception){
		String message = exception.getMessage();
		return new ResponseEntity<ApiResponses>(new ApiResponses(message, HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(FileNotSupportedException.class)
	public ResponseEntity<ApiResponses> handleFileNotSupportedException(FileNotSupportedException exception){
		String message = exception.getMessage();
		return new ResponseEntity<ApiResponses>(new ApiResponses(message, HttpStatus.NOT_ACCEPTABLE), HttpStatus.NOT_ACCEPTABLE);
	}
	
	@ExceptionHandler(IllegalStateException.class)
	public ResponseEntity<ApiResponses> handleIllegalStateException(IllegalStateException exception){
		String message = exception.getMessage();
		return new ResponseEntity<ApiResponses>(new ApiResponses(message, HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);	
	}
	
	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<ApiResponses> handleDataIntegrityViolationException(DataIntegrityViolationException exception){
		String message = exception.getMessage();
		return new ResponseEntity<ApiResponses>(new ApiResponses("Data Integrity Violation "+message, HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
	}
	
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, Object>> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception){
		List<ObjectError> errors = exception.getBindingResult().getAllErrors();
		Map<String, Object> response = new HashMap<>();
		errors.stream().forEach(object -> {
			String message = object.getDefaultMessage();
			String field = ((FieldError)object).getField();
			response.put(field, message);
		});
		return new ResponseEntity<Map<String,Object>>(response, HttpStatus.NOT_ACCEPTABLE);
	}
	
	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ResponseEntity<ApiResponses> methodArgumentMismatchException(MethodArgumentTypeMismatchException exception) {
		String message = exception.getMessage();
		ApiResponses response = new ApiResponses(message, HttpStatus.BAD_REQUEST);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	}
	
	@ExceptionHandler(PropertyReferenceException.class)
	public ResponseEntity<ApiResponses> handlePropertyRefrenceException(PropertyReferenceException exception) {
		String message = exception.getMessage();
		exception.printStackTrace();
		return new ResponseEntity<ApiResponses>(new ApiResponses(message, HttpStatus.BAD_REQUEST),
				HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<ApiResponses> handleConstraintViolationException(ConstraintViolationException exception) {
		String message = exception.getLocalizedMessage();
		return new ResponseEntity<ApiResponses>(new ApiResponses(message, HttpStatus.BAD_REQUEST),
				HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	public ResponseEntity<ApiResponses> httpRequestMethodNotSupported(HttpRequestMethodNotSupportedException exception) {
		String message = exception.getMessage();
		ApiResponses response = new ApiResponses(message, HttpStatus.METHOD_NOT_ALLOWED);
		return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(response);
	}

	@ExceptionHandler(MultipartException.class)
	public ResponseEntity<ApiResponses> multipartException(MultipartException exception) {
		String message = exception.getMessage();
		ApiResponses response = new ApiResponses(message, HttpStatus.INTERNAL_SERVER_ERROR);
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
	}

	@ExceptionHandler(MissingServletRequestPartException.class)
	public ResponseEntity<ApiResponses> missingRequestPartException(MissingServletRequestPartException exception) {
		String message = exception.getMessage();
		ApiResponses response = new ApiResponses(message, HttpStatus.BAD_REQUEST);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	}
	
	@ExceptionHandler(StringIndexOutOfBoundsException.class)
	public ResponseEntity<ApiResponses> handleStringIndexOutOfBoundsException(StringIndexOutOfBoundsException exception){
		String message = "File Can Not Be Empty !!";
		ApiResponses response = new ApiResponses(message, HttpStatus.BAD_REQUEST);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	}
	
	@ExceptionHandler(NoSuchElementException.class)
	public ResponseEntity<ApiResponses> handleNoSuchElementException(NoSuchElementException exception){
		String message = "Empty Cart";
		ApiResponses response = new ApiResponses(message, HttpStatus.BAD_GATEWAY);
		return new ResponseEntity<ApiResponses>(response, HttpStatus.BAD_GATEWAY);
	}
	
	@ExceptionHandler(UsernameNotFoundException.class)
	public ResponseEntity<ApiResponses> handleUserNameNotFoundException(UsernameNotFoundException exception){
		String message = exception.getMessage();
		ApiResponses response = new ApiResponses(message, HttpStatus.NOT_FOUND);
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
	}
	
	@ExceptionHandler(AccessDeniedException.class)
	 public ResponseEntity<ApiResponses> accessDeniedException(AccessDeniedException
	 exception){
	 String message = exception.getLocalizedMessage();
	 ApiResponses response = new ApiResponses(message, HttpStatus.UNAUTHORIZED);
	 return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
	 }

	@ExceptionHandler(ExpiredJwtException.class)
	 public ResponseEntity<ApiResponses> accessExpiredJwtExceptiom(ExpiredJwtException
	 exception){
	 String message = exception.getMessage();
	 ApiResponses response = new ApiResponses(message, HttpStatus.FORBIDDEN);
	 return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
	 }
}

