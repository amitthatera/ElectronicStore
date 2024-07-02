package com.estore.services;

import java.util.List;
import java.util.Optional;

import com.estore.dto.PageableResponse;
import com.estore.dto.UserDTO;
import com.estore.entities.User;

public interface UserServices {

	UserDTO createUser(UserDTO userDto);

	UserDTO updateUser(UserDTO userDto, String userId);

	void deleteUser(String userId);

	UserDTO getUserById(String userId);

	PageableResponse<UserDTO> getAllUsers(int pageNumber, int pageSize, String sortBy, String sortDir);

	UserDTO getUserByEmail(String emailAddress);

	List<UserDTO> getUserByKeyword(String keyword);
	
	Optional<User> findUserByEmail(String email);
}
