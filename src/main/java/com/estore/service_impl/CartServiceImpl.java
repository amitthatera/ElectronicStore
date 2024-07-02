package com.estore.service_impl;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.estore.custom_exception.ApiException;
import com.estore.custom_exception.ResourceNotFoundException;
import com.estore.dto.CartDTO;
import com.estore.entities.Cart;
import com.estore.entities.CartItem;
import com.estore.entities.Product;
import com.estore.entities.User;
import com.estore.repository.CartItemRepository;
import com.estore.repository.CartRepository;
import com.estore.repository.ProductRepository;
import com.estore.repository.UserRepositiry;
import com.estore.services.CartService;
import com.estore.utility.AddToCartRequest;

@Service
public class CartServiceImpl implements CartService {

	private final UserRepositiry userRepo;

	private final ProductRepository productRepo;

	private final CartRepository cartRepo;

	private final CartItemRepository itemRepo;

	private final ModelMapper modelMapper;

	public CartServiceImpl(UserRepositiry userRepo, ProductRepository productRepo, CartRepository cartRepo, CartItemRepository itemRepo, ModelMapper modelMapper) {
		this.userRepo = userRepo;
		this.productRepo = productRepo;
		this.cartRepo = cartRepo;
		this.itemRepo = itemRepo;
		this.modelMapper = modelMapper;
	}

	@Override
	public CartDTO addToCart(String userId, AddToCartRequest request) {

		String productId = request.getProductId();
		int quantity = request.getQuantity();

		if (quantity <= 0) {
			throw new ApiException("Requested Quantity is Not Valid !!");
		}

		Product product = this.productRepo.findById(productId)
				.orElseThrow(() -> new ResourceNotFoundException("Product Not Exist With ID: " + productId));

		User user = this.userRepo.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User Not Exist With ID: " + userId));

		Cart cart;
		try {
			cart = this.cartRepo.findByUser(user).orElseThrow(() -> new ResourceNotFoundException("User Not Exist"));
		} catch (NoSuchElementException e) {
			cart = new Cart();
			cart.setCartId(generateRandomId());
		}

		AtomicReference<Boolean> updated = new AtomicReference<>(false);
		List<CartItem> items = cart.getCartItems();
		items.forEach(item -> {
			if (item.getProduct().getProductId().equals(productId)) {
				item.setQuantity(quantity);
				item.setTotalPrice(quantity * product.getDiscountedPrice());
				updated.set(true);
			}
		});

        if (!updated.get()) {
			CartItem cartItem = CartItem.builder()
					.quantity(quantity)
					.product(product)
					.totalPrice(quantity * product.getDiscountedPrice())
					.cart(cart)
					.build();

			cart.getCartItems().add(cartItem);
		}

		cart.setUser(user);

		Cart updatedCart = this.cartRepo.save(cart);
		return this.modelMapper.map(updatedCart, CartDTO.class);
	}

	@Override
	public void removeFromCart(long cartItemId) {
		CartItem item = this.itemRepo.findById(cartItemId)
				.orElseThrow(() -> new ResourceNotFoundException("Item Not EXist in Cart"));
		this.itemRepo.delete(item);
	}

	@Override
	public void clearCart(String userId) {
		User user = this.userRepo.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User Not Exist With ID: " + userId));
		Cart cart = this.cartRepo.findByUser(user).orElseThrow(() -> new ResourceNotFoundException("User Not Exist"));
		cart.getCartItems().clear();
		cartRepo.save(cart);
	}
	
	@Override
	public CartDTO getCartByUser(String userId) {
		User user = this.userRepo.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User Not Exist With ID: " + userId));
		Cart cart = this.cartRepo.findByUser(user).orElseThrow(() -> new ResourceNotFoundException("User Not Exist"));
		return this.modelMapper.map(cart, CartDTO.class);
	}

	public String generateRandomId() {
		String randomID = UUID.randomUUID().toString();
		randomID = randomID.substring(0, 11).toUpperCase();
		return randomID;
	}
}
