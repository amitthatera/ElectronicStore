package com.estore.dto;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import com.estore.entities.Images;
import com.fasterxml.jackson.annotation.JsonFormat;

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
public class ProductDTO {

	@JsonIgnore
	private String productId;
	
	@NotBlank(message = "Name can not be blank !!")
	private String productName;
	
	private double actualPrice;
	
	private double discountedPrice;
	
	private String discountPercentage;
	
	@Size(min = 100, message = "Description must be in 100 characters !!")
	private String productDesc;
	
	@JsonFormat(pattern = "dd/MM/yyyy")
	private Date creationDate;
	
	@JsonFormat(pattern = "dd/MM/yyyy")
	private Date updationDate;
	
	@Builder.Default
	private Boolean inStock = true;
	
	@Builder.Default
	private Boolean isActive = true;
	
	private int productStock;
	
	private SubCategoryDTO subCategory;
	
	@Builder.Default
	private Set<Images> productImages = new HashSet<>();
	
}
