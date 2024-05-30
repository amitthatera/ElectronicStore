package com.estore.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.estore.entities.Cart;
import com.estore.entities.User;

public interface CartRepository extends JpaRepository<Cart, String> {

	Optional<Cart> findByUser(User user);
}
