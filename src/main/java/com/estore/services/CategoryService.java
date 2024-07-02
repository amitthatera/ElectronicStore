package com.estore.services;

import java.util.List;

import com.estore.dto.CategoryDTO;
import com.estore.dto.PageableResponse;

public interface CategoryService {

	CategoryDTO createCategory(CategoryDTO categoryDto);

	CategoryDTO updateCategory(CategoryDTO categoryDto, String categoryId);

	void deleteCategory(String categoryId);

	CategoryDTO getCategoryById(String categoryId);

	List<CategoryDTO > searchCategory(String keyword);

	PageableResponse<CategoryDTO> getAllCategory(int pageNumber, int pageSize, String sortBy, String sortDir);

}
