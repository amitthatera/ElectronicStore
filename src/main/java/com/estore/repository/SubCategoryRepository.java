package com.estore.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.estore.entities.SubCategory;

public interface SubCategoryRepository extends JpaRepository<SubCategory, String>{

	List<SubCategory> findBySubCategoryNameContainingIgnoreCase(String categoryName);
}
