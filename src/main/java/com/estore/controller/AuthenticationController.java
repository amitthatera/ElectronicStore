package com.estore.controller;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.estore.custom_exception.ApiException;
import com.estore.dto.UserDTO;
import com.estore.jwt_utils.JwtAuthRequest;
import com.estore.jwt_utils.JwtAuthResponse;
import com.estore.jwt_utils.JwtUtils;
import com.estore.service_impl.UserServiceImpl;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;



@RestController
@RequestMapping("/auth")
@Tag(name = "AUTHENTICATION_CONTROLLER", description = "API's related to authenticate user.")
public class AuthenticationController {

	private final UserServiceImpl userService;

	private final UserDetailsService userDetailsService;

	private final JwtUtils jwtUtils;

	private final ModelMapper modelMapper;

	private final AuthenticationManager authManager;

	public AuthenticationController(UserServiceImpl userService, UserDetailsService userDetailsService,
									JwtUtils jwtUtils, ModelMapper modelMapper, AuthenticationManager authManager) {
		this.userService = userService;
		this.userDetailsService = userDetailsService;
		this.jwtUtils = jwtUtils;
		this.modelMapper = modelMapper;
		this.authManager = authManager;
	}

	@PostMapping("/register")
	@Operation(
			description = "Register User API", 
			responses = {
			@ApiResponse(responseCode = "400", ref = "badRequestApi"),
			@ApiResponse(responseCode = "500", ref = "internalServerErrorApi"),
			@ApiResponse(responseCode = "201", description = "Registration Successfully!",
			content = @Content(mediaType = "application/json", examples = {
					@ExampleObject(value = "{\"code\" : 201, \"Status\" : \"Created!\", \"Message\" :\"User Registered!\"}") })) })
	public ResponseEntity<UserDTO> registerUser(@Valid @RequestBody UserDTO userDto){
		UserDTO user = this.userService.createUser(userDto);
		return new ResponseEntity<>(user, HttpStatus.CREATED);
	}
	
	@PostMapping("/login")
	@Operation(
			description = "Login User API", 
			responses = {
			@ApiResponse(responseCode = "400", ref = "badRequestApi"),
			@ApiResponse(responseCode = "500", ref = "internalServerErrorApi"),
			@ApiResponse(responseCode = "200", description = "User LoggedIn!", 
			content = @Content(mediaType = "application/json", examples = {
					@ExampleObject(value = "{\"code\" : 200, \"Status\" : \"OK!\", \"Message\" :\"Login Successful!\"}") })) })

	public ResponseEntity<JwtAuthResponse> login(@RequestBody JwtAuthRequest request){
		authenticateDetails(request.getUsername(), request.getPassword());
		UserDetails userDetails = this.userDetailsService.loadUserByUsername(request.getUsername());
		String token = this.jwtUtils.generateToken(userDetails);
		UserDTO user = this.modelMapper.map(userDetails, UserDTO.class);
		
		JwtAuthResponse response = JwtAuthResponse
	            .builder()
	            .token(token)
	            .user(user)
	            .build();
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	private void authenticateDetails(String username, String password) {
		UsernamePasswordAuthenticationToken authToken = new 
				UsernamePasswordAuthenticationToken(username, password);
		try {
			authManager.authenticate(authToken);
		}catch(BadCredentialsException e) {
			throw new ApiException("Incorrect Username or Password !!");
		}
	}
}
