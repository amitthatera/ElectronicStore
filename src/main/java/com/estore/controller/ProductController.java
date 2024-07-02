package com.estore.controller;

import java.util.List;
import java.util.Set;

import jakarta.validation.Valid;

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

import com.estore.dto.PageableResponse;
import com.estore.dto.ProductDTO;
import com.estore.entities.Images;
import com.estore.service_impl.FileServiceImpl;
import com.estore.service_impl.ProductServiceImpl;
import com.estore.utility.ApiResponses;
import com.estore.utility.AppConstant;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/product")
@Tag(name = "PRODUCT_CONTROLLER", description = "API's related to perform product crud operation.")
public class ProductController {

	private final ProductServiceImpl productService;

	private final FileServiceImpl fileService;

	private final String path;

	public ProductController(ProductServiceImpl productService, FileServiceImpl fileService,
							 @Value("${product.image.path}")String path) {
		this.productService = productService;
		this.fileService = fileService;
		this.path = path;
	}

	@PreAuthorize("hasRole('ADMIN')")
	@Operation(
			security = @SecurityRequirement(name = "token"),
			description = "Create Product API", 
			responses = {
			@ApiResponse(responseCode = "400", ref = "badRequestApi"),
			@ApiResponse(responseCode = "500", ref = "internalServerErrorApi"),
			@ApiResponse(responseCode = "201", description = "Product Created Successfully!",
			content = @Content(mediaType = "application/json", examples = {
					@ExampleObject(value = "{\"code\" : 201, \"Status\" : \"Created!\", \"Message\" :\"Product Created!\"}") })) })
	@PostMapping(value = "/", consumes = "multipart/form-data")
	public ResponseEntity<ProductDTO> createProduct(@Valid
			@RequestPart("productData") 
	        @Parameter(schema = @Schema(type = "string", format = "binary")) ProductDTO productDto,
			@RequestPart("productImages") 
	        @ArraySchema(schema = @Schema(type = "string", format = "binary"))  MultipartFile[] files) {
		Set<Images> images = this.fileService.uploadFile(files, path);
		productDto.setProductImages(images);
		ProductDTO product = this.productService.createProduct(productDto);
		return new ResponseEntity<>(product, HttpStatus.CREATED);
	}

	@PreAuthorize("hasRole('ADMIN')")
	@Operation(
			security = @SecurityRequirement(name = "token"),
			description = "Create Product Using SubCategory ID API", 
			responses = {
			@ApiResponse(responseCode = "400", ref = "badRequestApi"),
			@ApiResponse(responseCode = "500", ref = "internalServerErrorApi"),
			@ApiResponse(responseCode = "201", description = "Product Created Successfully!",
			content = @Content(mediaType = "application/json", examples = {
					@ExampleObject(value = "{\"code\" : 201, \"Status\" : \"Created!\", \"Message\" :\"Product Created!\"}") })) })
	@PostMapping(value = "/subCategory/{subCategoryId}", consumes = "multipart/form-data")
	public ResponseEntity<ProductDTO> createProduct(@Valid
			@RequestPart("productData")
	        @Parameter(schema = @Schema(type = "string", format = "binary")) ProductDTO productDto,
			@RequestPart("productImages") 
	        @ArraySchema(schema = @Schema(type = "string", format = "binary")) MultipartFile[] files, @PathVariable String subCategoryId) {
		Set<Images> images = this.fileService.uploadFile(files, path);
		productDto.setProductImages(images);
		ProductDTO product = this.productService.createProduct(productDto, subCategoryId);
		return new ResponseEntity<>(product, HttpStatus.CREATED);
	}

	@PreAuthorize("hasRole('ADMIN')")
	@Operation(
			security = @SecurityRequirement(name = "token"),
			description = "Update Product API", 
			responses = {
			@ApiResponse(responseCode = "400", ref = "badRequestApi"),
			@ApiResponse(responseCode = "500", ref = "internalServerErrorApi"),
			@ApiResponse(responseCode = "201", description = "Product Updated Successfully!",
			content = @Content(mediaType = "application/json", examples = {
					@ExampleObject(value = "{\"code\" : 201, \"Status\" : \"Created!\", \"Message\" :\"Product Updated!\"}") })) })
	@PutMapping(value = "/{productId}", consumes = "multipart/form-data")
	public ResponseEntity<ProductDTO> updateProduct(@Valid
			@RequestPart(value = "productData", required = false) 
			@Parameter(schema = @Schema(type = "string", format = "binary")) ProductDTO productDto,
			@RequestPart(value = "productImages", required = false) 
	        @ArraySchema(schema = @Schema(type = "string", format = "binary")) MultipartFile[] files,
			@PathVariable String productId) {
		Set<Images> images = this.fileService.uploadFile(files, path);
		productDto.setProductImages(images);
		ProductDTO product = this.productService.updateProduct(productDto, productId);
		return new ResponseEntity<>(product, HttpStatus.CREATED);
	}

	@PreAuthorize("hasRole('ADMIN')")
	@Operation(
			security = @SecurityRequirement(name = "token"),
			description = "Update Product SubCategory API", 
			responses = {
			@ApiResponse(responseCode = "400", ref = "badRequestApi"),
			@ApiResponse(responseCode = "500", ref = "internalServerErrorApi"),
			@ApiResponse(responseCode = "201", description = "Product Category Updated Successfully!",
			content = @Content(mediaType = "application/json", examples = {
					@ExampleObject(value = "{\"code\" : 201, \"Status\" : \"Created!\", \"Message\" :\"Product Category Updated!\"}") })) })
	@PutMapping("/{productId}/subCategory/{subCategoryId}")
	public ResponseEntity<ProductDTO> updateProductCategory(@PathVariable String productId,
			@PathVariable String subCategoryId) {
		ProductDTO product = this.productService.updateSubCategory(productId, subCategoryId);
		return new ResponseEntity<>(product, HttpStatus.CREATED);

	}

	@PreAuthorize("hasRole('ADMIN')")
	@Operation(
			security = @SecurityRequirement(name = "token"),
			description = "Delete Product API", 
			responses = {
			@ApiResponse(responseCode = "400", ref = "badRequestApi"),
			@ApiResponse(responseCode = "500", ref = "internalServerErrorApi"),
			@ApiResponse(responseCode = "200", description = "Product Deleted Successfully!",
			content = @Content(mediaType = "application/json", examples = {
					@ExampleObject(value = "{\"code\" : 200, \"Status\" : \"OK!\", \"Message\" :\"Product Deleted!\"}") })) })
	@DeleteMapping("/{productId}")
	public ResponseEntity<ApiResponses> deleteProduct(@PathVariable String productId) {
		this.productService.deleteProduct(productId);
		return new ResponseEntity<>(new ApiResponses("Product Deleted Successfully.", HttpStatus.OK),
                HttpStatus.OK);
	}

	@GetMapping("/{productId}")
	@Operation(
			description = "Get Product By ID API", 
			responses = {
			@ApiResponse(responseCode = "400", ref = "badRequestApi"),
			@ApiResponse(responseCode = "500", ref = "internalServerErrorApi"),
			@ApiResponse(responseCode = "200", description = "Product Fetched!", 
			content = @Content(mediaType = "application/json", examples = {
					@ExampleObject(value = "{\"code\" : 200, \"Status\" : \"OK!\", \"Message\" :\"Product Fetched!\"}") })) })
	public ResponseEntity<ProductDTO> getProductById(@PathVariable String productId) {
		ProductDTO product = this.productService.getByProductId(productId);
		return new ResponseEntity<>(product, HttpStatus.OK);
	}

	@GetMapping("/search/{keyword}")
	@Operation(
			description = "Search Product API", 
			responses = {
			@ApiResponse(responseCode = "400", ref = "badRequestApi"),
			@ApiResponse(responseCode = "500", ref = "internalServerErrorApi"),
			@ApiResponse(responseCode = "200", description = "OK!", 
			content = @Content(mediaType = "application/json", examples = {
					@ExampleObject(value = "{\"code\" : 200, \"Status\" : \"OK!\", \"Message\" :\"OK!\"}") })) })
	public ResponseEntity<List<ProductDTO>> searchProduct(@PathVariable String keyword) {
		List<ProductDTO> products = this.productService.searchProduct(keyword);
		return new ResponseEntity<>(products, HttpStatus.OK);
	}

	@GetMapping("/active")
	@Operation(
			description = "Get Active Product API", 
			responses = {
			@ApiResponse(responseCode = "400", ref = "badRequestApi"),
			@ApiResponse(responseCode = "500", ref = "internalServerErrorApi"),
			@ApiResponse(responseCode = "200", description = "OK!", 
			content = @Content(mediaType = "application/json", examples = {
					@ExampleObject(value = "{\"code\" : 200, \"Status\" : \"OK!\", \"Message\" :\"OK!\"}") })) })
	public ResponseEntity<PageableResponse<ProductDTO>> getAllActiveProducts(
			@RequestParam(value = "pageNumber", defaultValue = AppConstant.PAGE_NUMBER, required = false) int pageNumber,
			@RequestParam(value = "pageSize", defaultValue = AppConstant.PAGE_SIZE, required = false) int pageSize,
			@RequestParam(value = "sortBy", defaultValue = AppConstant.PRODUCT_SORT_BY, required = false) String sortBy,
			@RequestParam(value = "sortDir", defaultValue = AppConstant.SORT_DIR, required = false) String sortDir) {
		PageableResponse<ProductDTO> response = this.productService.getAllActiveProduct(pageNumber, pageSize, sortBy,
				sortDir);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@GetMapping()
	@Operation(
			description = "Get All Product API", 
			responses = {
			@ApiResponse(responseCode = "400", ref = "badRequestApi"),
			@ApiResponse(responseCode = "500", ref = "internalServerErrorApi"),
			@ApiResponse(responseCode = "200", description = "OK!", 
			content = @Content(mediaType = "application/json", examples = {
					@ExampleObject(value = "{\"code\" : 200, \"Status\" : \"OK!\", \"Message\" :\"OK!\"}") })) })
	public ResponseEntity<PageableResponse<ProductDTO>> getAllProducts(
			@RequestParam(value = "pageNumber", defaultValue = AppConstant.PAGE_NUMBER, required = false) int pageNumber,
			@RequestParam(value = "pageSize", defaultValue = AppConstant.PAGE_SIZE, required = false) int pageSize,
			@RequestParam(value = "sortBy", defaultValue = AppConstant.PRODUCT_SORT_BY, required = false) String sortBy,
			@RequestParam(value = "sortDir", defaultValue = AppConstant.SORT_DIR, required = false) String sortDir) {
		PageableResponse<ProductDTO> response = this.productService.getAllProduct(pageNumber, pageSize, sortBy,
				sortDir);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
}
