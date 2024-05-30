package com.estore.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.estore.dto.PageableResponse;
import com.estore.dto.UserDTO;
import com.estore.service_impl.FileServiceImpl;
import com.estore.service_impl.UserServiceImpl;
import com.estore.utility.ApiResponses;
import com.estore.utility.AppConstant;
import com.estore.utility.FileResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;

import com.estore.custom_exception.ResourceNotFoundException;

@RestController
@RequestMapping("/api/v1/user")
@Tag(name = "USER_CONTROLLER", description = "API's related to perform user operation.")
public class UserController {

	@Autowired
	private UserServiceImpl userService;

	@Autowired
	private FileServiceImpl fileService;

	@Value("${user.profile.image.path}")
	private String path;

	@PostMapping("/")
	@Operation(
			security = @SecurityRequirement(name = "token"),
			description = "Create User API", 
			responses = {
			@ApiResponse(responseCode = "400", ref = "badRequestApi"),
			@ApiResponse(responseCode = "500", ref = "internalServerErrorApi"),
			@ApiResponse(responseCode = "201", description = "User Created Succesfully!", 
			content = @Content(mediaType = "application/json", examples = {
					@ExampleObject(value = "{\"code\" : 201, \"Status\" : \"Created!\", \"Message\" :\"User Created!\"}") })) })
	public ResponseEntity<UserDTO> createUser(@Valid @RequestBody UserDTO userDto) {
		UserDTO user = this.userService.createUser(userDto);
		return new ResponseEntity<UserDTO>(user, HttpStatus.CREATED);
	}

	@PreAuthorize("#userId == authentication.principal.userId")
	@Operation(
			security = @SecurityRequirement(name = "token"),
			description = "Update User API", 
			responses = {
			@ApiResponse(responseCode = "400", ref = "badRequestApi"),
			@ApiResponse(responseCode = "500", ref = "internalServerErrorApi"),
			@ApiResponse(responseCode = "201", description = "User Updated Succesfully!", 
			content = @Content(mediaType = "application/json", examples = {
					@ExampleObject(value = "{\"code\" : 201, \"Status\" : \"Created!\", \"Message\" :\"User Updated!\"}") })) })
	@PutMapping("/{userId}")
	public ResponseEntity<UserDTO> updateUser(@Valid @RequestBody UserDTO userDto, @PathVariable String userId) {
		UserDTO user = this.userService.updateUser(userDto, userId);
		return new ResponseEntity<UserDTO>(user, HttpStatus.CREATED);
	}

	@PreAuthorize("hasRole('ADMIN') or #userId == authentication.principal.userId")
	@Operation(
			security = @SecurityRequirement(name = "token"),
			description = "Delete User API", 
			responses = {
			@ApiResponse(responseCode = "400", ref = "badRequestApi"),
			@ApiResponse(responseCode = "500", ref = "internalServerErrorApi"),
			@ApiResponse(responseCode = "200", description = "User Deleted Succesfully!", 
			content = @Content(mediaType = "application/json", examples = {
					@ExampleObject(value = "{\"code\" : 200, \"Status\" : \"OK!\", \"Message\" :\"User Deleted!\"}") })) })
	@DeleteMapping("/{userId}")
	public ResponseEntity<ApiResponses> deleteUser(@PathVariable String userId) {
		this.userService.deleteUser(userId);
		return new ResponseEntity<ApiResponses>(new ApiResponses("User Deleted Successfully. ", HttpStatus.OK),
				HttpStatus.OK);
	}

	@GetMapping("/{userId}")
	@Operation(
			description = "Get User By \'ID\'", 
			responses = {
			@ApiResponse(responseCode = "400", ref = "badRequestApi"),
			@ApiResponse(responseCode = "500", ref = "internalServerErrorApi"),
			@ApiResponse(responseCode = "200", description = "User Found!", 
			content = @Content(mediaType = "application/json", examples = {
					@ExampleObject(value = "{\"code\" : 200, \"Status\" : \"OK!\", \"Message\" :\"OK!\"}") })) })
	public ResponseEntity<UserDTO> getUserById(@PathVariable String userId) {
		UserDTO user = this.userService.getUserById(userId);
		return new ResponseEntity<UserDTO>(user, HttpStatus.OK);
	}

	@GetMapping("/email/{email}")
	@Operation(
			description = "Get User By \'Email\'", 
			responses = {
			@ApiResponse(responseCode = "400", ref = "badRequestApi"),
			@ApiResponse(responseCode = "500", ref = "internalServerErrorApi"),
			@ApiResponse(responseCode = "200", description = "User Found!", 
			content = @Content(mediaType = "application/json", examples = {
					@ExampleObject(value = "{\"code\" : 200, \"Status\" : \"OK!\", \"Message\" :\"OK!\"}") })) })
	public ResponseEntity<UserDTO> getUserByEmail(@PathVariable String email) {
		UserDTO user = this.userService.getUserByEmail(email);
		return new ResponseEntity<UserDTO>(user, HttpStatus.OK);
	}

	@GetMapping("/search/{keyword}")
	@Operation(
			description = "Search User By \'Keyword\'", 
			responses = {
			@ApiResponse(responseCode = "400", ref = "badRequestApi"),
			@ApiResponse(responseCode = "500", ref = "internalServerErrorApi"),
			@ApiResponse(responseCode = "200", description = "User Found!", 
			content = @Content(mediaType = "application/json", examples = {
					@ExampleObject(value = "{\"code\" : 200, \"Status\" : \"OK!\", \"Message\" :\"OK!\"}") })) })
	public ResponseEntity<List<UserDTO>> searchUser(@PathVariable String keyword) {
		List<UserDTO> users = this.userService.getUserByKeyword(keyword);
		return new ResponseEntity<List<UserDTO>>(users, HttpStatus.OK);
	}

	@PreAuthorize("hasRole('ADMIN')")
	@Operation(
			security = @SecurityRequirement(name = "token"), 
			description = "Get All Users API", 
			responses = {
			@ApiResponse(responseCode = "400", ref = "badRequestApi"),
			@ApiResponse(responseCode = "500", ref = "internalServerErrorApi"),
			@ApiResponse(responseCode = "200", description = "Users Fetched Succesfully!", 
			content = @Content(mediaType = "application/json", examples = {
					@ExampleObject(value = "{\"code\" : 200, \"Status\" : \"OK!\", \"Message\" :\"User Fetched!\"}") })) })
	@GetMapping()
	public ResponseEntity<PageableResponse<UserDTO>> getAllUser(
			@RequestParam(value = "pageNumber", defaultValue = AppConstant.PAGE_NUMBER, required = false) Integer pageNumber,
			@RequestParam(value = "pageSize", defaultValue = AppConstant.PAGE_SIZE, required = false) Integer pageSize,
			@RequestParam(value = "sortBy", defaultValue = AppConstant.USER_SORT_BY, required = false) String sortBy,
			@RequestParam(value = "sortDir", defaultValue = AppConstant.SORT_DIR, required = false) String sortDir) {
		PageableResponse<UserDTO> users = this.userService.getAllUsers(pageNumber, pageSize, sortBy, sortDir);
		return new ResponseEntity<PageableResponse<UserDTO>>(users, HttpStatus.OK);
	}

	@PreAuthorize("#userId == authentication.principal.userId")
	@Operation(
			security = @SecurityRequirement(name = "token"), 
			description = "Upload Image API", 
			responses = {
			@ApiResponse(responseCode = "400", ref = "badRequestApi"),
			@ApiResponse(responseCode = "500", ref = "internalServerErrorApi"),
			@ApiResponse(responseCode = "200", description = "Image Uploaded Successfully!", 
			content = @Content(mediaType = "application/json", examples = {
					@ExampleObject(value = "{\"code\" : 200, \"Status\" : \"OK!\", \"Message\" :\"Image Uploaded!\"}") })) })

	@PostMapping(value = "/upload/{userId}", consumes = "multipart/form-data")
	public ResponseEntity<FileResponse> uploadFile(@RequestParam("image") MultipartFile file,
			@PathVariable String userId) {
		String image = this.fileService.uploadFile(file, path);
		UserDTO user = this.userService.getUserById(userId);
		user.setImageName(image);
		this.userService.updateUser(user, userId);
		return new ResponseEntity<FileResponse>(new FileResponse(image, "Image Uploaded Successfully", HttpStatus.OK),
				HttpStatus.OK);

	}

	@PreAuthorize("authentication.principal.userId == #userId")
	@Operation( 
			security = @SecurityRequirement(name = "token"),
			description = "Serve Image API", 
			responses = {
			@ApiResponse(responseCode = "400", ref = "badRequestApi"),
			@ApiResponse(responseCode = "500", ref = "internalServerErrorApi"),
			@ApiResponse(responseCode = "200", description = "Image Served!", 
			content = @Content(mediaType = "application/json", examples = {
					@ExampleObject(value = "{\"code\" : 200, \"Status\" : \"OK!\", \"Message\" :\"Image Served!\"}") })) })
	@GetMapping("/serve/{userId}")
	public void serveFile(@PathVariable String userId, HttpServletResponse response) {
		UserDTO user = this.userService.getUserById(userId);
		InputStream image = this.fileService.serveImage(path, user.getImageName());
		response.setContentType(MediaType.IMAGE_JPEG_VALUE);
		try {
			StreamUtils.copy(image, response.getOutputStream());
		} catch (IOException e) {
			throw new ResourceNotFoundException("NO IMAGE AVAILABLE !!");
		}
	}

}
