package com.estore.dto;

import javax.validation.constraints.NotBlank;

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
public class AddressDTO {

	private Long addressId;
	
	@NotBlank(message = "Please Enter Details !!")
	private String houseNumber;
	
	@NotBlank(message = "Please Enter Street !!")
	private String street;
	
	@NotBlank(message = "Please Enter Landmark !!")
	private String landmark;

	@NotBlank(message = "Please Enter Landmark !!")
	private String city;
	
	@NotBlank(message = "Please Enter State !!")
	private String state;
	
	@NotBlank(message = "Please Enter Postal Code !!")
	private Integer postalCode;
	
}
