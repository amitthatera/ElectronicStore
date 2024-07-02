package com.estore.service_impl;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.estore.custom_exception.ResourceNotFoundException;
import com.estore.dto.CategoryDTO;
import com.estore.dto.PageableResponse;
import com.estore.entities.Category;
import com.estore.repository.CategoryRepository;
import com.estore.services.CategoryService;
import com.estore.utility.Helper;

@Service
public class CategoryServiceImpl implements CategoryService {

	private final CategoryRepository categoryRepo;

	private final ModelMapper modelMapper;

	public CategoryServiceImpl(CategoryRepository categoryRepo, ModelMapper modelMapper) {
		this.categoryRepo = categoryRepo;
		this.modelMapper = modelMapper;
	}

	@Override
	public CategoryDTO createCategory(CategoryDTO categoryDto) {
		Category category = this.modelMapper.map(categoryDto, Category.class);
		category.setCategoryId(generateRandomId());
		Category newCategory = this.categoryRepo.save(category);
		return this.modelMapper.map(newCategory, CategoryDTO.class);
	}

	@Override
	public CategoryDTO updateCategory(CategoryDTO categoryDto, String categoryId) {
		Category category = this.categoryRepo.findById(categoryId)
				.orElseThrow(() -> new ResourceNotFoundException("Category Not Exist With ID: " + categoryId));
		category.setCategoryName(categoryDto.getCategoryName());
		category.setCategoryDesc(categoryDto.getCategoryDesc());
		Category updatedCategory = this.categoryRepo.save(category);
		return this.modelMapper.map(updatedCategory, CategoryDTO.class);
	}

	@Override
	public void deleteCategory(String categoryId) {
		Category category = this.categoryRepo.findById(categoryId)
				.orElseThrow(() -> new ResourceNotFoundException("Category Not Exist With ID: " + categoryId));
		this.categoryRepo.delete(category);
	}

	@Override
	public CategoryDTO getCategoryById(String categoryId) {
		Category category = this.categoryRepo.findById(categoryId)
				.orElseThrow(() -> new ResourceNotFoundException("Category Not Exist With ID: " + categoryId));
		return this.modelMapper.map(category, CategoryDTO.class);
	}

	@Override
	public List<CategoryDTO> searchCategory(String keyword) {
		List<Category> category = this.categoryRepo.findByCategoryNameContainingIgnoreCase(keyword);
        return category.stream()
				.map(cat -> this.modelMapper.map(cat, CategoryDTO.class)).collect(Collectors.toList());
	}

	@Override
	public PageableResponse<CategoryDTO> getAllCategory(int pageNumber, int pageSize, String sortBy,
			String sortDir) {
		Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());
		Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
		Page<Category> category = this.categoryRepo.findAll(pageable);
        return Helper.getPageableResponse(category, CategoryDTO.class);
	}

	public String generateRandomId() {
		String randomID = UUID.randomUUID().toString();
		randomID = randomID.substring(0, 5).toUpperCase();
		return randomID;
	}
}
