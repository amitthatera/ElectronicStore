package com.estore.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.estore.entities.Product;
import com.estore.entities.SubCategory;

public interface ProductRepository extends JpaRepository<Product, String> {
	
	Page<Product> findBySubCategory(SubCategory subCategory, Pageable pagebale);
	
	Page<Product> findByisActiveTrue(Pageable pageable);

	List<Product> findByProductNameContainingIgnoreCase(String productName);
}
