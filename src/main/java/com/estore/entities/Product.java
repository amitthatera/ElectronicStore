package com.estore.entities;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

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
@Table(name = "products")
public class Product {

	@Id
	private String productId;
	
	@Column(nullable = false)
	private String productName;
	
	@Column(nullable = false)
	private double actualPrice;
	
	private double discountedPrice;

	private String discountPercentage;
	
	@Column(length = 10000)
	private String productDesc;
	
	@CreationTimestamp
	@Column(updatable = false)
	private Date creationDate;
	
	@UpdateTimestamp
	private Date updationDate;
	
	private Boolean inStock;
	
	private Boolean isActive;
	
	private int productStock;
	
	@ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
	@JoinColumn(name = "subCategory_id", referencedColumnName = "subCategoryId")
	private SubCategory subCategory;
	
	@Builder.Default
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinTable(name = "product_images",
			joinColumns = {
			@JoinColumn(name = "product_id", referencedColumnName = "productId")}, inverseJoinColumns = {
		    @JoinColumn(name = "image_id", referencedColumnName = "imageId")
	})
	private Set<Images> productImages = new HashSet<>();
}
