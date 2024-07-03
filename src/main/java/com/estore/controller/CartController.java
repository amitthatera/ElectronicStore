package com.estore.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.estore.dto.CartDTO;
import com.estore.service_impl.CartServiceImpl;
import com.estore.utility.AddToCartRequest;
import com.estore.utility.ApiResponses;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/cart")
@Tag(name = "CART_CONTROLLER", description = "API's use to perform cart related activities.")
public class CartController {

	private final CartServiceImpl cartService;

	public CartController(CartServiceImpl cartService) {
		this.cartService = cartService;
	}

	@PreAuthorize("#userId == authentication.principal.userId")
	@Operation(
			security = @SecurityRequirement(name = "token"),
			description = "Add To Cart API", 
			responses = {
			@ApiResponse(responseCode = "400", ref = "badRequestApi"),
			@ApiResponse(responseCode = "500", ref = "internalServerErrorApi"),
			@ApiResponse(responseCode = "201", description = "Item Added Successfully!",
			content = @Content(mediaType = "application/json", examples = {
					@ExampleObject(value = "{\"code\" : 201, \"Status\" : \"Created!\", \"Message\" :\"Item Added!\"}") })) })
	@PostMapping("/{userId}")
	public ResponseEntity<CartDTO> addToCart(@PathVariable String userId, @RequestBody AddToCartRequest request){
		CartDTO cart = this.cartService.addToCart(userId, request);
		return new ResponseEntity<>(cart, HttpStatus.CREATED);
	}
	
	@PreAuthorize("hasRole('NORMAL') or hasRole('ADMIN')")
	@Operation(
			security = @SecurityRequirement(name = "token"),
			description = "Remove Item From Cart API", 
			responses = {
			@ApiResponse(responseCode = "400", ref = "badRequestApi"),
			@ApiResponse(responseCode = "500", ref = "internalServerErrorApi"),
			@ApiResponse(responseCode = "200", description = "Item Removed Successfully!",
			content = @Content(mediaType = "application/json", examples = {
					@ExampleObject(value = "{\"code\" : 200, \"Status\" : \"OK!\", \"Message\" :\"Item Removed!\"}") })) })
	@DeleteMapping("/cart_item/{itemId}")
	public ResponseEntity<ApiResponses> removeFromCart(@PathVariable long itemId){
		this.cartService.removeFromCart(itemId);
		return new ResponseEntity<>(new ApiResponses("Item Removed From Cart !!", HttpStatus.OK), HttpStatus.OK);
	}
	
	@PreAuthorize("authentication.principal.userId == #userId")
	@Operation(
			security = @SecurityRequirement(name = "token"),
			description = "Clear Cart API", 
			responses = {
			@ApiResponse(responseCode = "400", ref = "badRequestApi"),
			@ApiResponse(responseCode = "500", ref = "internalServerErrorApi"),
			@ApiResponse(responseCode = "200", description = "Cart Cleared!", 
			content = @Content(mediaType = "application/json", examples = {
					@ExampleObject(value = "{\"code\" : 200, \"Status\" : \"OK!\", \"Message\" :\"Cart Cleared!\"}") })) })
	@DeleteMapping("/{userId}")
	public ResponseEntity<ApiResponses> clearCart(@PathVariable String userId){
		this.cartService.clearCart(userId);
		return new ResponseEntity<>(new ApiResponses("Cart Cleared !!", HttpStatus.OK), HttpStatus.OK);
	}
	
	@PreAuthorize("#userId == authentication.principal.userId")
	@Operation(
			security = @SecurityRequirement(name = "token"),
			description = "Get User Cart API", 
			responses = {
			@ApiResponse(responseCode = "400", ref = "badRequestApi"),
			@ApiResponse(responseCode = "500", ref = "internalServerErrorApi"),
			@ApiResponse(responseCode = "200", description = "OK!", 
			content = @Content(mediaType = "application/json", examples = {
					@ExampleObject(value = "{\"code\" : 200, \"Status\" : \"OK!\", \"Message\" :\"OK!\"}") })) })
	@GetMapping("/{userId}")
	public ResponseEntity<CartDTO> getCart(@PathVariable String userId){
		CartDTO cart = this.cartService.getCartByUser(userId);
		return new ResponseEntity<>(cart, HttpStatus.OK);
	}
}
