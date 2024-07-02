package com.estore.service_impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.estore.custom_exception.ResourceNotFoundException;
import com.estore.dto.PageableResponse;
import com.estore.dto.UserDTO;
import com.estore.entities.Roles;
import com.estore.entities.User;
import com.estore.repository.RolesRepository;
import com.estore.repository.UserRepositiry;
import com.estore.services.UserServices;
import com.estore.utility.AppConstant;
import com.estore.utility.Helper;

@Service
public class UserServiceImpl implements UserServices {

	private final UserRepositiry userRepo;

	private final ModelMapper modelMapper;

	private final BCryptPasswordEncoder passwordEncoder;

	private final RolesRepository roleRepo;

	private final String path;

	public UserServiceImpl(UserRepositiry userRepo, ModelMapper modelMapper,
						   BCryptPasswordEncoder passwordEncoder, RolesRepository roleRepo, @Value("${user.profile.image.path}")String path) {
		this.userRepo = userRepo;
		this.modelMapper = modelMapper;
		this.passwordEncoder = passwordEncoder;
		this.roleRepo = roleRepo;
		this.path = path;
	}

	@Override
	public UserDTO createUser(UserDTO userDto) {
		User user = this.modelMapper.map(userDto, User.class);
		boolean userExist = this.userRepo.findByEmailAddressIgnoreCase(userDto.getEmailAddress()).isPresent();
		if (userExist) {
			throw new IllegalStateException("Email already taken !!");
		}
		user.setUserId(generateRandomId());
		user.setUserPassword(this.passwordEncoder.encode(userDto.getPassword()));
		
		Roles role = this.roleRepo.findById(AppConstant.NORMAL).orElseThrow(() -> new ResourceNotFoundException("Role Not Exist!!"));
		user.getRoles().add(role);
		
		User newUser = this.userRepo.save(user);
		return this.modelMapper.map(newUser, UserDTO.class);
	}

	@Override
	public UserDTO updateUser(UserDTO userDto, String userId) {
		User user = this.userRepo.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User Not Exist With ID: " + userId));
		user.setFirstName(userDto.getFirstName());
		user.setLastName(userDto.getLastName());
		user.setMobileNumber(userDto.getMobileNumber());
		user.setImageName(userDto.getImageName());
		user.setIsActive(userDto.getIsActive());
		User updatedUser = this.userRepo.save(user);
		return this.modelMapper.map(updatedUser, UserDTO.class);
	}

	@Override
	public void deleteUser(String userId) {
		User user = this.userRepo.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User Not Exist With ID: " + userId));

		String fullPath = path + user.getImageName();

		Path file = Paths.get(fullPath);
		try {
			Files.delete(file);
		} catch (IOException e) {
			this.userRepo.delete(user);
		}
		
		this.userRepo.delete(user);
	}

	@Override
	public UserDTO getUserById(String userId) {
		User user = this.userRepo.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User Not Exist With ID: " + userId));
		return this.modelMapper.map(user, UserDTO.class);
	}

	@Override
	public PageableResponse<UserDTO> getAllUsers(int pageNumber, int pageSize, String sortBy, String sortDir) {
		Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());
		Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
		Page<User> page = this.userRepo.findAll(pageable);
        return Helper.getPageableResponse(page, UserDTO.class);
	}

	@Override
	public UserDTO getUserByEmail(String emailAddress) {
		User user = this.userRepo.findByEmailAddressIgnoreCase(emailAddress)
				.orElseThrow(() -> new ResourceNotFoundException("User Not Exist With Email: " + emailAddress));
		return this.modelMapper.map(user, UserDTO.class);
	}

	@Override
	public List<UserDTO> getUserByKeyword(String keyword) {
		List<User> users = this.userRepo.findByFirstNameContainingIgnoreCase(keyword);
        return users.stream().map(u -> this.modelMapper.map(u, UserDTO.class))
				.collect(Collectors.toList());
	}
	
	@Override
	public Optional<User> findUserByEmail(String email) {
		return this.userRepo.findByEmailAddressIgnoreCase(email);
	}
	
	public String generateRandomId() {
		String randomID = UUID.randomUUID().toString();
		randomID = randomID.substring(0, 13).toUpperCase();
		return randomID;
	}

	
}
