package com.estore.controller;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import com.estore.entities.User;
import com.estore.jwt_utils.JwtAuthRequest;
import com.estore.jwt_utils.JwtAuthResponse;
import com.estore.jwt_utils.JwtUtils;
import com.estore.service_impl.UserServiceImpl;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;



@RestController
@RequestMapping("/api/auth-user")
@Tag(name = "AUTHENTICATION_CONTROLLER", description = "API's related to authenticate user.")
public class AuthenticationController {
	
	@Autowired
	private UserServiceImpl userService;
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	private JwtUtils jwtUtils;

	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private AuthenticationManager authManager;
	
	@Value("${googleClientId}")
	private String googleClientId;
	
	@Value("${newPassword}")
	private String newPassword;
	
	Logger logger = LoggerFactory.getLogger(AuthenticationController.class);
	
	
	@PostMapping("/register")
	@Operation(
			description = "Register User API", 
			responses = {
			@ApiResponse(responseCode = "400", ref = "badRequestApi"),
			@ApiResponse(responseCode = "500", ref = "internalServerErrorApi"),
			@ApiResponse(responseCode = "201", description = "Registration Succesfully!", 
			content = @Content(mediaType = "application/json", examples = {
					@ExampleObject(value = "{\"code\" : 201, \"Status\" : \"Created!\", \"Message\" :\"User Registered!\"}") })) })
	public ResponseEntity<UserDTO> registerUser(@Valid @RequestBody UserDTO userDto){
		UserDTO user = this.userService.createUser(userDto);
		return new ResponseEntity<UserDTO>(user, HttpStatus.CREATED);
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
		return new ResponseEntity<JwtAuthResponse>(response, HttpStatus.OK);
	}
	
	@PostMapping("/google")
	public ResponseEntity<JwtAuthResponse> loginWithGoogle(@RequestBody Map<String, Object> data) throws IOException{
		String idToken = data.get("idToken").toString();
		
		NetHttpTransport httpTransport = new NetHttpTransport();
		JsonFactory jsonFactory = GsonFactory.getDefaultInstance();
		
		GoogleIdTokenVerifier.Builder verifier =  new GoogleIdTokenVerifier.Builder(httpTransport, jsonFactory).setAudience(Collections.singleton(googleClientId));
		GoogleIdToken googleIdToken = GoogleIdToken.parse(verifier.getJsonFactory(), idToken);
		GoogleIdToken.Payload payload = googleIdToken.getPayload();
		
		logger.info("Payload: {} ", payload);
		
		String email = payload.getEmail();
		
		User user = userService.findUserByEmail(email).get();
		
		ResponseEntity<JwtAuthResponse> response = null;
		if(user != null) {
			response = this.login(JwtAuthRequest.builder()
					.username(user.getEmailAddress())
					.password(newPassword)
					.build());
		}else {
			this.saveUser(email, data.get("firstName").toString(), data.get("lastName").toString());
		}
		
		return response;
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
	
	private User saveUser(String email, String fname, String lName) {
		UserDTO user = UserDTO.builder()
				.firstName(fname)
				.lastName(lName)
				.emailAddress(email)
				.password(newPassword)
				.roles(new HashSet<>())
				.build();
		
		UserDTO newUser = this.userService.createUser(user);
		return this.modelMapper.map(newUser, User.class);
	}

}
