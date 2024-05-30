package com.estore.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.estore.dto.OrderDTO;
import com.estore.dto.PageableResponse;
import com.estore.service_impl.OrderServiceImpl;
import com.estore.utility.ApiResponses;
import com.estore.utility.AppConstant;
import com.estore.utility.OrderStatus;
import com.estore.utility.PaymentStatus;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/order")
@Tag(name = "ORDER_CONTROLLER", description = "API's related to manage user orders.")
public class OrderController {

	@Autowired
	private OrderServiceImpl orderService;

	@PreAuthorize("#userId == authentication.principal.userId")
	@Operation(
			security = @SecurityRequirement(name = "token"),
			description = "Create Order API", 
			responses = {
			@ApiResponse(responseCode = "400", ref = "badRequestApi"),
			@ApiResponse(responseCode = "500", ref = "internalServerErrorApi"),
			@ApiResponse(responseCode = "201", description = "Order Creted Succesfully!", 
			content = @Content(mediaType = "application/json", examples = {
					@ExampleObject(value = "{\"code\" : 201, \"Status\" : \"Created!\", \"Message\" :\"Order Created!\"}") })) })
	@PostMapping("/user/{userId}")
	public ResponseEntity<OrderDTO> createOrder(@Valid @RequestBody OrderDTO orderDto, @PathVariable String userId) {
		OrderDTO order = this.orderService.createOrder(orderDto, userId);
		return new ResponseEntity<OrderDTO>(order, HttpStatus.CREATED);
	}

	
	@PreAuthorize("hasRole('NORMAL')")
	@Operation(
			security = @SecurityRequirement(name = "token"),
			description = "Remove Order API", 
			responses = {
			@ApiResponse(responseCode = "400", ref = "badRequestApi"),
			@ApiResponse(responseCode = "500", ref = "internalServerErrorApi"),
			@ApiResponse(responseCode = "200", description = "Order Removed Succesfully!", 
			content = @Content(mediaType = "application/json", examples = {
					@ExampleObject(value = "{\"code\" : 200, \"Status\" : \"OK!\", \"Message\" :\"Order Removed!\"}") })) })
	@DeleteMapping("/{orderId}")
	public ResponseEntity<ApiResponses> removeFromOrder(@PathVariable String orderId) {
		this.orderService.removeOrder(orderId);
		return new ResponseEntity<ApiResponses>(new ApiResponses("Order Removed Successfully !!", HttpStatus.OK),
				HttpStatus.OK);
	}

    @PreAuthorize("hasRole('NORMAL')")
	@Operation(
			security = @SecurityRequirement(name = "token"),
			description = "Get Order API", 
			responses = {
			@ApiResponse(responseCode = "400", ref = "badRequestApi"),
			@ApiResponse(responseCode = "500", ref = "internalServerErrorApi"),
			@ApiResponse(responseCode = "200", description = "OK!", 
			content = @Content(mediaType = "application/json", examples = {
					@ExampleObject(value = "{\"code\" : 200, \"Status\" : \"OK!\", \"Message\" :\"OK!\"}") })) })
	@GetMapping("/{orderId}")
	public ResponseEntity<OrderDTO> getOrderById(@PathVariable String orderId) {
		OrderDTO order = this.orderService.getOrderById(orderId);
		return new ResponseEntity<OrderDTO>(order, HttpStatus.OK);
	}

    @PreAuthorize("hasRole('ADMIN') or hasRole('NORMAL')")
	@Operation(
			security = @SecurityRequirement(name = "token"),
			description = "Get All Order API", 
			responses = {
			@ApiResponse(responseCode = "400", ref = "badRequestApi"),
			@ApiResponse(responseCode = "500", ref = "internalServerErrorApi"),
			@ApiResponse(responseCode = "200", description = "OK!", 
			content = @Content(mediaType = "application/json", examples = {
					@ExampleObject(value = "{\"code\" : 200, \"Status\" : \"OK!\", \"Message\" :\"OK!\"}") })) })
	@GetMapping()
	public ResponseEntity<PageableResponse<OrderDTO>> getAllOrders(
			@RequestParam(value = "pageNumber", defaultValue = AppConstant.PAGE_NUMBER, required = false) Integer pageNumber,
			@RequestParam(value = "pageSize", defaultValue = AppConstant.PAGE_SIZE, required = false) Integer pageSize,
			@RequestParam(value = "sortBy", defaultValue = AppConstant.ORDER_SORT_BY, required = false) String sortBy,
			@RequestParam(value = "sortDir", defaultValue = AppConstant.SORT_DIR, required = false) String sortDir) {
		PageableResponse<OrderDTO> orders = this.orderService.getAllOrders(pageNumber, pageSize, sortBy, sortDir);
		return new ResponseEntity<PageableResponse<OrderDTO>>(orders, HttpStatus.OK);
	}

	@PreAuthorize("hasRole('ADMIN')")
	@Operation(
			security = @SecurityRequirement(name = "token"),
			description = "Update Payment Status API", 
			responses = {
			@ApiResponse(responseCode = "400", ref = "badRequestApi"),
			@ApiResponse(responseCode = "500", ref = "internalServerErrorApi"),
			@ApiResponse(responseCode = "201", description = "Payment Received!", 
			content = @Content(mediaType = "application/json", examples = {
					@ExampleObject(value = "{\"code\" : 201, \"Status\" : \"OK!\", \"Message\" :\"Payment Received!\"}") })) })
	@PutMapping("/{orderId}/payment_status")
	public ResponseEntity<OrderDTO> updatePaymentStatus(@PathVariable String orderId,
			@RequestParam PaymentStatus paymentStatus) {
       OrderDTO order = this.orderService.updatePaymentStatus(orderId, paymentStatus);
       return new ResponseEntity<OrderDTO>(order, HttpStatus.OK);
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@Operation(
			security = @SecurityRequirement(name = "token"),
			description = "Update Order Status API", 
			responses = {
			@ApiResponse(responseCode = "400", ref = "badRequestApi"),
			@ApiResponse(responseCode = "500", ref = "internalServerErrorApi"),
			@ApiResponse(responseCode = "201", description = "OK!", 
			content = @Content(mediaType = "application/json", examples = {
					@ExampleObject(value = "{\"code\" : 201, \"Status\" : \"OK!\", \"Message\" :\"OK!\"}") })) })
	@PutMapping("/{orderId}/order_status")
	public ResponseEntity<OrderDTO> updateOrderStatus(@PathVariable String orderId,
			@RequestParam OrderStatus orderStatus) {
       OrderDTO order = this.orderService.updateOrderStatus(orderId, orderStatus);
       return new ResponseEntity<OrderDTO>(order, HttpStatus.OK);
	}
}
