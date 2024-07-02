package com.estore.dto;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {

	private String userId;
	
	@NotBlank(message = "First Name can not be empty !!")
	@Size(min = 3, message = "First Name must contain 3 characters !!")
	private String firstName;
	
	@NotBlank(message = "First Name can not be empty !!")
	@Size(min = 3, message = "First Name must contain 3 characters !!")
	private String lastName;
	
	@NotBlank(message = "Email can not be empty !!")
	private String emailAddress;
	
	@NotBlank(message = "Password can not be empty !!")
	private String password;
	
	private String mobileNumber;
	
	private String gender;
	
	@JsonFormat(pattern = "dd/MM/yyyy")
	private Date dateOfBirth;
	
	@Builder.Default
	private String imageName = "default.png";
	
	@Builder.Default
	private Boolean isActive = true;
	
	@JsonIgnore
	@Builder.Default
	private Set<AddressDTO> address = new HashSet<>();
	
	@Builder.Default
	private Set<RoleDTO> roles = new HashSet<>();
}
