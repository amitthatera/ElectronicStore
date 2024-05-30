package com.estore.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

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
public class CartDTO {

	private String cartId;
	
	@CreationTimestamp
	private Date createdDate;
	
	private UserDTO user;

	@Builder.Default
	private List<CartItemDTO> cartItems = new ArrayList<>();
}
