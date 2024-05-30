package com.estore.service_impl;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.estore.custom_exception.ApiException;
import com.estore.custom_exception.ResourceNotFoundException;
import com.estore.dto.OrderDTO;
import com.estore.dto.PageableResponse;
import com.estore.entities.Address;
import com.estore.entities.Cart;
import com.estore.entities.CartItem;
import com.estore.entities.Order;
import com.estore.entities.OrderItem;
import com.estore.entities.User;
import com.estore.repository.CartRepository;
import com.estore.repository.OrderRepository;
import com.estore.repository.UserRepositiry;
import com.estore.services.OrderService;
import com.estore.utility.Helper;
import com.estore.utility.OrderStatus;
import com.estore.utility.PaymentStatus;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Service
public class OrderServiceImpl implements OrderService {

	@Autowired
	private OrderRepository orderRepo;
	
	@Autowired
	private UserRepositiry userRepo;
	
	@Autowired 
	private CartRepository cartRepo;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Override
	public OrderDTO createOrder(OrderDTO orderDto, String userId) {
		User user = this.userRepo.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User Not Exist With ID: "+ userId));
		
		Cart cart = this.cartRepo.findByUser(user)
				.orElseThrow(() -> new ResourceNotFoundException("Empty Cart !!"));
		
		List<CartItem> items = cart.getCartItems();
		
		if(items.size() <= 0) {
			throw new ApiException("Invaliad Number of Items in Cart !!");
		}
		
		Address address = this.modelMapper.map(orderDto.getBillingAddress(), Address.class);
		
		Order order = Order.builder()
				.orderId(UUID.randomUUID().toString())
				.orderStatus(orderDto.getOrderStatus())
				.paymentStatus(orderDto.getPaymentStatus())
				.billingName(orderDto.getBillingName())
				.billingContact(orderDto.getBillingContact())
				.billingAddress(address)
				.deliveredDate(null)
				.user(user)
				.build();
		
		AtomicReference<Double> orderAmount = new AtomicReference<>(0.00);
		List<OrderItem> orderItems = items.stream().map((cartItem) -> {
			OrderItem orderItem = OrderItem.builder()
					.quantity(cartItem.getQuantity())
					.totalPrice(cartItem.getQuantity() * cartItem.getProduct().getDiscountedPrice())
					.product(cartItem.getProduct())
					.order(order)
					.build();
			
			orderAmount.set(orderAmount.get() + orderItem.getTotalPrice());
			
			return orderItem;
		}).collect(Collectors.toList());
		
		order.setOrderAmount(orderAmount.get());
		order.setItems(orderItems);
		
		Order savedOrder = this.orderRepo.save(order);
		
		cart.getCartItems().clear();
		cartRepo.save(cart);
		
		return this.modelMapper.map(savedOrder, OrderDTO.class);
	}

	@Override
	public void removeOrder(String orderId) {
		Order order = this.orderRepo.findById(orderId)
				.orElseThrow(() ->  new ResourceNotFoundException("Order Not Exist With ID: "+orderId));
		this.orderRepo.delete(order);
	}

	@Override
	public OrderDTO getOrderById(String orderId) {
		Order order = this.orderRepo.findById(orderId)
				.orElseThrow(() ->  new ResourceNotFoundException("Order Not Exist With ID: "+orderId));
		return this.modelMapper.map(order, OrderDTO.class);
	}

	@Override
	public List<OrderDTO> getOrderByUser(String userId) {
		User user = this.userRepo.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User Not Exist With ID: "+ userId));
		
    	List<Order> orders = this.orderRepo.findByUser(user);
    	List<OrderDTO> orderDto = orders.stream().map(order -> this.modelMapper.map(order, OrderDTO.class))
    			.collect(Collectors.toList());
		return orderDto;
	}

	@Override
	public PageableResponse<OrderDTO> getAllOrders(Integer pageNumber, Integer pageSize, String sortBy,
			String sortDir) {
		Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());
		Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
		Page<Order> pages = this.orderRepo.findAll(pageable);
		PageableResponse<OrderDTO> response = Helper.getPageableResponse(pages, OrderDTO.class);
		return response;
	}

	@Override
	public OrderDTO updatePaymentStatus(String orderId, PaymentStatus paymentStatus) {
		Order order = this.orderRepo.findById(orderId)
				.orElseThrow(() ->  new ResourceNotFoundException("Order Not Exist With ID: "+orderId));
		order.setPaymentStatus(paymentStatus);
		Order updatedOrder = this.orderRepo.save(order);
		return this.modelMapper.map(updatedOrder, OrderDTO.class);
	}

	@Override
	public OrderDTO updateOrderStatus(String orderId, OrderStatus orderStatus) {
		Order order = this.orderRepo.findById(orderId)
				.orElseThrow(() ->  new ResourceNotFoundException("Order Not Exist With ID: "+orderId));
		if(order.getPaymentStatus().equals(PaymentStatus.PAID)) {
			order.setOrderStatus(orderStatus);
		}else {
			throw new ApiException("Payment Not Received !!");
		}
		Order updatedOrder = this.orderRepo.save(order);
		return this.modelMapper.map(updatedOrder, OrderDTO.class);
	}

}
