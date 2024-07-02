package com.estore.services;

import com.estore.dto.CartDTO;
import com.estore.utility.AddToCartRequest;

public interface CartService {

	CartDTO addToCart(String userId, AddToCartRequest request);

	void removeFromCart(long cartItemId);
	
	void clearCart(String userId);
	
	CartDTO getCartByUser(String userId);
}
