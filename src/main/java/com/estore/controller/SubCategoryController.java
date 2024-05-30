package com.estore.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.estore.dto.SubCategoryDTO;
import com.estore.dto.PageableResponse;
import com.estore.dto.ProductDTO;
import com.estore.service_impl.FileServiceImpl;
import com.estore.service_impl.ProductServiceImpl;
import com.estore.service_impl.SubCategoryServiceImpl;
import com.estore.utility.ApiResponses;
import com.estore.utility.AppConstant;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/sub_category")
@Tag(name = "SUB_CATEGORY_CONTROLLER", description = "API's related to perform SubCategory crud operation.")
public class SubCategoryController {

	@Autowired
	private SubCategoryServiceImpl subCategoryService;

	@Autowired
	private FileServiceImpl fileService;

	@Autowired
	private ProductServiceImpl productService;

	@Value("${category.image.path}")
	private String path;

	@PreAuthorize("hasRole('ADMIN')")
	@Operation(security = @SecurityRequirement(name = "token"), description = "Create SubCategory API", responses = {
			@ApiResponse(responseCode = "400", ref = "badRequestApi"),
			@ApiResponse(responseCode = "500", ref = "internalServerErrorApi"),
			@ApiResponse(responseCode = "201", description = "SubCategory Created Succesfully!", content = @Content(mediaType = "application/json", examples = {
					@ExampleObject(value = "{\"code\" : 201, \"Status\" : \"Created!\", \"Message\" :\"SubCategory Created!\"}") })) })
	@PostMapping(value = "/", consumes = "multipart/form-data")
	public ResponseEntity<SubCategoryDTO> createSubCategory(@Valid 
			@RequestPart("subCategoryData") @Parameter(schema = @Schema(type = "string", format = "binary")) SubCategoryDTO subCategory,
			@RequestPart("image") MultipartFile file) {
		String imageName = this.fileService.uploadFile(file, path);
		subCategory.setImageName(imageName);
		SubCategoryDTO subCategoryDto = this.subCategoryService.createSubCategory(subCategory);
		return new ResponseEntity<SubCategoryDTO>(subCategoryDto, HttpStatus.CREATED);
	}

	@PreAuthorize("hasRole('ADMIN')")
	@Operation(security = @SecurityRequirement(name = "token"), 
	    description = "Create SubCategory By Category ID API", responses = {
			@ApiResponse(responseCode = "400", ref = "badRequestApi"),
			@ApiResponse(responseCode = "500", ref = "internalServerErrorApi"),
			@ApiResponse(responseCode = "201", description = "SubCategory Created Succesfully!", content = @Content(mediaType = "application/json", examples = {
					@ExampleObject(value = "{\"code\" : 201, \"Status\" : \"Created!\", \"Message\" :\"SubCategory Created!\"}") })) })
	@PostMapping(value = "/category/{categoryId}", consumes = "multipart/form-data")
	public ResponseEntity<SubCategoryDTO> createSubCategory(@Valid 
			@RequestPart("subCategoryData") @Parameter(schema = @Schema(type = "string", format = "binary")) SubCategoryDTO subCategory,
			@RequestPart("image") MultipartFile file, @PathVariable String categoryId) {
		String imageName = this.fileService.uploadFile(file, path);
		subCategory.setImageName(imageName);
		SubCategoryDTO subCategoryDto = this.subCategoryService.createSubCategory(subCategory, categoryId);
		return new ResponseEntity<SubCategoryDTO>(subCategoryDto, HttpStatus.CREATED);
	}

	
	@PreAuthorize("hasRole('ADMIN')")
	@Operation(security = @SecurityRequirement(name = "token"), description = "Update SubCategory API", responses = {
			@ApiResponse(responseCode = "400", ref = "badRequestApi"),
			@ApiResponse(responseCode = "500", ref = "internalServerErrorApi"),
			@ApiResponse(responseCode = "201", description = "SubCategory Updated Succesfully!", content = @Content(mediaType = "application/json", examples = {
					@ExampleObject(value = "{\"code\" : 201, \"Status\" : \"Created!\", \"Message\" :\"SubCategory Updated!\"}") })) })
	@PutMapping(value = "/{subCategoryId}", consumes = "multipart/form-data")
	public ResponseEntity<SubCategoryDTO> updateSubCategory(@Valid 
			@RequestPart("subCategoryData")
			@Parameter(schema = @Schema(type = "string", format = "binary")) SubCategoryDTO subCategory,
			@RequestPart("image") MultipartFile file, @PathVariable String subCategoryId) {
		String imageName = this.fileService.uploadFile(file, path);
		subCategory.setImageName(imageName);
		SubCategoryDTO SubCategoryDto = this.subCategoryService.updateSubCategory(subCategory, subCategoryId);
		return new ResponseEntity<SubCategoryDTO>(SubCategoryDto, HttpStatus.CREATED);
	}

	@PreAuthorize("hasRole('ADMIN')")
	@Operation(security = @SecurityRequirement(name = "token"), 
	           description = "Update SubCategory Category API", responses = {
			@ApiResponse(responseCode = "400", ref = "badRequestApi"),
			@ApiResponse(responseCode = "500", ref = "internalServerErrorApi"),
			@ApiResponse(responseCode = "201", description = "SubCategory Updated Succesfully!", content = @Content(mediaType = "application/json", examples = {
					@ExampleObject(value = "{\"code\" : 201, \"Status\" : \"Created!\", \"Message\" :\"SubCategory Updated!\"}") })) })
	@PutMapping(value = "/{subCategoryId}/category/{categoryId}")
	public ResponseEntity<SubCategoryDTO> updateCategory(@Valid @PathVariable String subCategoryId, @PathVariable String categoryId) {
		SubCategoryDTO SubCategoryDto = this.subCategoryService.updateCategory(subCategoryId, categoryId);
		return new ResponseEntity<SubCategoryDTO>(SubCategoryDto, HttpStatus.CREATED);
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@Operation(security = @SecurityRequirement(name = "token"), description = "Delete SubCategory API", responses = {
			@ApiResponse(responseCode = "400", ref = "badRequestApi"),
			@ApiResponse(responseCode = "500", ref = "internalServerErrorApi"),
			@ApiResponse(responseCode = "200", description = "SubCategory Deleted Succesfully!", content = @Content(mediaType = "application/json", examples = {
					@ExampleObject(value = "{\"code\" : 200, \"Status\" : \"OK!\", \"Message\" :\"SubCategory Deleted!\"}") })) })
	@DeleteMapping("/{subCategoryId}")
	public ResponseEntity<ApiResponses> deleteSubCategory(@PathVariable String subCategoryId) {
		this.subCategoryService.deleteSubCategory(subCategoryId);
		return new ResponseEntity<ApiResponses>(new ApiResponses("SubCategory Deleted Successfully !!", HttpStatus.OK),
				HttpStatus.OK);
	}

	@GetMapping("/{subCategoryId}")
	@Operation(description = "Get SubCategory By ID API", responses = {
			@ApiResponse(responseCode = "400", ref = "badRequestApi"),
			@ApiResponse(responseCode = "500", ref = "internalServerErrorApi"),
			@ApiResponse(responseCode = "200", description = "SubCategory Fetched!", content = @Content(mediaType = "application/json", examples = {
					@ExampleObject(value = "{\"code\" : 200, \"Status\" : \"OK!\", \"Message\" :\"SubCategory Fetched!\"}") })) })
	public ResponseEntity<SubCategoryDTO> getSubCategoryById(@PathVariable String subCategoryId) {
		SubCategoryDTO SubCategory = this.subCategoryService.getSubCategoryById(subCategoryId);
		return new ResponseEntity<SubCategoryDTO>(SubCategory, HttpStatus.OK);
	}

	@GetMapping("/search/{keyword}")
	@Operation(description = "Search SubCategory API", responses = {
			@ApiResponse(responseCode = "400", ref = "badRequestApi"),
			@ApiResponse(responseCode = "500", ref = "internalServerErrorApi"),
			@ApiResponse(responseCode = "200", description = "OK!", content = @Content(mediaType = "application/json", examples = {
					@ExampleObject(value = "{\"code\" : 200, \"Status\" : \"OK!\", \"Message\" :\"OK!\"}") })) })
	public ResponseEntity<List<SubCategoryDTO>> searchSubCategory(@PathVariable String keyword) {
		List<SubCategoryDTO> SubCategory = this.subCategoryService.searchSubCategory(keyword);
		return new ResponseEntity<List<SubCategoryDTO>>(SubCategory, HttpStatus.OK);
	}

	@GetMapping()
	@Operation(description = "Get All SubCategory API", responses = {
			@ApiResponse(responseCode = "400", ref = "badRequestApi"),
			@ApiResponse(responseCode = "500", ref = "internalServerErrorApi"),
			@ApiResponse(responseCode = "200", description = "OK!", content = @Content(mediaType = "application/json", examples = {
					@ExampleObject(value = "{\"code\" : 200, \"Status\" : \"OK!\", \"Message\" :\"OK!\"}") })) })
	public ResponseEntity<PageableResponse<SubCategoryDTO>> getAllSubCategory(
			@RequestParam(value = "pageNumber", defaultValue = AppConstant.PAGE_NUMBER, required = false) Integer pageNumber,
			@RequestParam(value = "pageSize", defaultValue = AppConstant.PAGE_SIZE, required = false) Integer pageSize,
			@RequestParam(value = "sortBy", defaultValue = AppConstant.SUB_CATEGORY_SORT_BY, required = false) String sortBy,
			@RequestParam(value = "sortDir", defaultValue = AppConstant.SORT_DIR, required = false) String sortDir) {
		PageableResponse<SubCategoryDTO> response = this.subCategoryService.getAllSubCategory(pageNumber, pageSize,
				sortBy, sortDir);
		return new ResponseEntity<PageableResponse<SubCategoryDTO>>(response, HttpStatus.OK);
	}

	@GetMapping("/{subCategoryId}/products")
	@Operation(description = "Get All Product By SubCategory ID API", responses = {
			@ApiResponse(responseCode = "400", ref = "badRequestApi"),
			@ApiResponse(responseCode = "500", ref = "internalServerErrorApi"),
			@ApiResponse(responseCode = "200", description = "OK!", content = @Content(mediaType = "application/json", examples = {
					@ExampleObject(value = "{\"code\" : 200, \"Status\" : \"OK!\", \"Message\" :\"OK!\"}") })) })
	public ResponseEntity<PageableResponse<ProductDTO>> getAllProductBySubCategoryId(@PathVariable String subCategoryId,
			@RequestParam(value = "pageNumber", defaultValue = AppConstant.PAGE_NUMBER, required = false) Integer pageNumber,
			@RequestParam(value = "pageSize", defaultValue = AppConstant.PAGE_SIZE, required = false) Integer pageSize,
			@RequestParam(value = "sortBy", defaultValue = AppConstant.PRODUCT_SORT_BY, required = false) String sortBy,
			@RequestParam(value = "sortDir", defaultValue = AppConstant.SORT_DIR, required = false) String sortDir) {
		PageableResponse<ProductDTO> response = this.productService.getBySubCategoryId(subCategoryId,pageNumber, pageSize,
				sortBy, sortDir);
		return new ResponseEntity<PageableResponse<ProductDTO>>(response, HttpStatus.OK);
	}
}
