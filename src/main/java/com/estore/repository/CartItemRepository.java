package com.estore.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.estore.entities.CartItem;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

	
}
