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

public class OrderItemDTO {

	private long orderItemId;
	private int quantity;
	private double totalPrice;
	private ProductDTO product;
	
}
