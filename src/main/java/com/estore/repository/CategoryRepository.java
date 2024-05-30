package com.estore.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.estore.entities.Category;

public interface CategoryRepository extends JpaRepository<Category, String> {

	List<Category> findByCategoryNameContainingIgnoreCase(String categoryName);
}
