package com.estore.dto;

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
public class CartItemDTO {

	private Long cartItemId;
	
	private Integer quantity;
	
	private Double totalPrice;

	private ProductDTO product;
	
}
