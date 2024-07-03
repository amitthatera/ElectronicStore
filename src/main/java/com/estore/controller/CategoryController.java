package com.estore.controller;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.estore.dto.CategoryDTO;
import com.estore.dto.PageableResponse;
import com.estore.service_impl.CategoryServiceImpl;
import com.estore.utility.ApiResponses;
import com.estore.utility.AppConstant;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/category")
@Tag(name = "CATEGORY_CONTROLLER", description = "API's related to perform category crud operation.")
public class CategoryController {

	private final CategoryServiceImpl categoryService;

	public CategoryController(CategoryServiceImpl categoryService) {
		this.categoryService = categoryService;
	}

	@PreAuthorize("hasRole('ADMIN')")
	@Operation(security = @SecurityRequirement(name = "token"), description = "Create Category API", responses = {
			@ApiResponse(responseCode = "400", ref = "badRequestApi"),
			@ApiResponse(responseCode = "500", ref = "internalServerErrorApi"),
			@ApiResponse(responseCode = "201", description = "Category Created Successfully!", content = @Content(mediaType = "application/json", examples = {
					@ExampleObject(value = "{\"code\" : 201, \"Status\" : \"Created!\", \"Message\" :\"Category Created!\"}") })) })
	@PostMapping("/")
	public ResponseEntity<CategoryDTO> createCategory(@Valid @RequestBody CategoryDTO category) {
		CategoryDTO categoryDto = this.categoryService.createCategory(category);
		return new ResponseEntity<>(categoryDto, HttpStatus.CREATED);
	}

	@PreAuthorize("hasRole('ADMIN')")
	@Operation(security = @SecurityRequirement(name = "token"), description = "Update Category API", responses = {
			@ApiResponse(responseCode = "400", ref = "badRequestApi"),
			@ApiResponse(responseCode = "500", ref = "internalServerErrorApi"),
			@ApiResponse(responseCode = "201", description = "Category Updated Successfully!", content = @Content(mediaType = "application/json", examples = {
					@ExampleObject(value = "{\"code\" : 201, \"Status\" : \"Created!\", \"Message\" :\"Category Updated!\"}") })) })
	@PutMapping("/{categoryId}")
	public ResponseEntity<CategoryDTO> updateCategory(@Valid @RequestBody CategoryDTO category,
			@PathVariable String categoryId) {
		CategoryDTO categoryDto = this.categoryService.updateCategory(category, categoryId);
		return new ResponseEntity<>(categoryDto, HttpStatus.CREATED);
	}

	@PreAuthorize("hasRole('ADMIN')")
	@Operation(security = @SecurityRequirement(name = "token"), description = "Delete Category API", responses = {
			@ApiResponse(responseCode = "400", ref = "badRequestApi"),
			@ApiResponse(responseCode = "500", ref = "internalServerErrorApi"),
			@ApiResponse(responseCode = "200", description = "Category Deleted Successfully!", content = @Content(mediaType = "application/json", examples = {
					@ExampleObject(value = "{\"code\" : 200, \"Status\" : \"OK!\", \"Message\" :\"Category Deleted!\"}") })) })
	@DeleteMapping("/{categoryId}")
	public ResponseEntity<ApiResponses> deleteCategory(@PathVariable String categoryId) {
		this.categoryService.deleteCategory(categoryId);
		return new ResponseEntity<>(new ApiResponses("Category Deleted Successfully !!", HttpStatus.OK),
                HttpStatus.OK);
	}

	@GetMapping("/{categoryId}")
	@Operation(description = "Get Category By ID API", responses = {
			@ApiResponse(responseCode = "400", ref = "badRequestApi"),
			@ApiResponse(responseCode = "500", ref = "internalServerErrorApi"),
			@ApiResponse(responseCode = "200", description = "Category Fetched!", content = @Content(mediaType = "application/json", examples = {
					@ExampleObject(value = "{\"code\" : 200, \"Status\" : \"OK!\", \"Message\" :\"Category Fetched!\"}") })) })
	public ResponseEntity<CategoryDTO> getCategoryById(@PathVariable String categoryId) {
		CategoryDTO category = this.categoryService.getCategoryById(categoryId);
		return new ResponseEntity<>(category, HttpStatus.OK);
	}

	@GetMapping("/search/{keyword}")
	@Operation(description = "Search Category API", responses = {
			@ApiResponse(responseCode = "400", ref = "badRequestApi"),
			@ApiResponse(responseCode = "500", ref = "internalServerErrorApi"),
			@ApiResponse(responseCode = "200", description = "OK!", content = @Content(mediaType = "application/json", examples = {
					@ExampleObject(value = "{\"code\" : 200, \"Status\" : \"OK!\", \"Message\" :\"OK!\"}") })) })
	public ResponseEntity<List<CategoryDTO>> searchCategory(@PathVariable String keyword) {
		List<CategoryDTO> category = this.categoryService.searchCategory(keyword);
		return new ResponseEntity<>(category, HttpStatus.OK);
	}

	@GetMapping()
	@Operation(description = "Get All Category API", responses = {
			@ApiResponse(responseCode = "400", ref = "badRequestApi"),
			@ApiResponse(responseCode = "500", ref = "internalServerErrorApi"),
			@ApiResponse(responseCode = "200", description = "OK!", content = @Content(mediaType = "application/json", examples = {
					@ExampleObject(value = "{\"code\" : 200, \"Status\" : \"OK!\", \"Message\" :\"OK!\"}") })) })
	public ResponseEntity<PageableResponse<CategoryDTO>> getAllCategory(
			@RequestParam(value = "pageNumber", defaultValue = AppConstant.PAGE_NUMBER, required = false) int pageNumber,
			@RequestParam(value = "pageSize", defaultValue = AppConstant.PAGE_SIZE, required = false) int pageSize,
			@RequestParam(value = "sortBy", defaultValue = AppConstant.CATEGORY_SORT_BY, required = false) String sortBy,
			@RequestParam(value = "sortDir", defaultValue = AppConstant.SORT_DIR, required = false) String sortDir) {
		PageableResponse<CategoryDTO> response = this.categoryService.getAllCategory(pageNumber, pageSize, sortBy,
				sortDir);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

}
