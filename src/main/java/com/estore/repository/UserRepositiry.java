package com.estore.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.estore.entities.User;

public interface UserRepositiry extends JpaRepository<User, String> {

	Optional<User> findByEmailAddressIgnoreCase(String emailAddress);
	
	List<User> findByFirstNameContainingIgnoreCase(String firstName);
}
