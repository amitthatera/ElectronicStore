package com.estore.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import jakarta.persistence.JoinColumn;
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
@Entity
@Table(name = "cart_items")
public class CartItem {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long cartItemId;
	
	private int quantity;
	
	private double totalPrice;

	@OneToOne
	@JoinColumn(name = "product_id", referencedColumnName = "productId")
	private Product product;
	
	@ManyToOne(fetch = FetchType.LAZY) 
	@JoinColumn(name = "cart_id", referencedColumnName = "cartId")
	private Cart cart;
}
