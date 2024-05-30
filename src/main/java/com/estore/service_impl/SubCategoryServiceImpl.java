package com.estore.service_impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.estore.custom_exception.ApiException;
import com.estore.custom_exception.ResourceNotFoundException;
import com.estore.dto.PageableResponse;
import com.estore.dto.SubCategoryDTO;
import com.estore.entities.Category;
import com.estore.entities.SubCategory;
import com.estore.repository.CategoryRepository;
import com.estore.repository.SubCategoryRepository;
import com.estore.services.SubCategoryService;
import com.estore.utility.Helper;

@Service
public class SubCategoryServiceImpl implements SubCategoryService {
	
	@Autowired
	private SubCategoryRepository subCategoryRepo;
	
	@Autowired
	private CategoryRepository categoryRepo;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Value("${category.image.path}")
	private String path;

	@Override
	public SubCategoryDTO createSubCategory(SubCategoryDTO subCategoryDTO) {
		SubCategory subCategory = this.modelMapper.map(subCategoryDTO, SubCategory.class);
		subCategory.setSubCategoryId(generateRandomId());
		SubCategory newSubCategory = this.subCategoryRepo.save(subCategory);
		return this.modelMapper.map(newSubCategory, SubCategoryDTO.class);
	}

	public SubCategoryDTO createSubCategory(SubCategoryDTO subCategoryDTO, String categoryId) {
		SubCategory subCategory = this.modelMapper.map(subCategoryDTO, SubCategory.class);
		
		Category category = this.categoryRepo.findById(categoryId)
				.orElseThrow(() -> new ResourceNotFoundException("Category Not Exist With ID: "+categoryId));
		
		subCategory.setCategory(category);
		subCategory.setSubCategoryId(generateRandomId());
		
		SubCategory newSubCategory = this.subCategoryRepo.save(subCategory);
		return this.modelMapper.map(newSubCategory, SubCategoryDTO.class);
	}
	
	@Override
	public SubCategoryDTO updateSubCategory(SubCategoryDTO subCategoryDTO, String subCategoryId) {
		SubCategory subCategory = this.subCategoryRepo.findById(subCategoryId)
				.orElseThrow(() -> new ResourceNotFoundException("SubCategory Not Exist With ID: "+subCategoryId));
		subCategory.setSubCategoryName(subCategoryDTO.getSubCategoryName());
		subCategory.setSubCategoryDesc(subCategoryDTO.getSubCategoryDesc());
		String filePath = path + subCategory.getImageName();
		try {
			Files.delete(Paths.get(filePath));
		}catch(IOException e) {
			throw new ApiException("File Not EXist");
		}
		subCategory.setImageName(subCategoryDTO.getImageName());
		SubCategory newSubCategory = this.subCategoryRepo.save(subCategory);
		return this.modelMapper.map(newSubCategory, SubCategoryDTO.class);
	}
	
	@Override
	public SubCategoryDTO updateCategory(String subCategoryId, String categoryId) {
		SubCategory subCategory = this.subCategoryRepo.findById(subCategoryId)
				.orElseThrow(() -> new ResourceNotFoundException("SubCategory Not Exist With ID: "+subCategoryId));
		
		Category category = this.categoryRepo.findById(categoryId)
				.orElseThrow(() -> new ResourceNotFoundException("Category Not Exist With ID: "+categoryId));
		
		subCategory.setCategory(category);
		SubCategory newSubCategory = this.subCategoryRepo.save(subCategory);
		return this.modelMapper.map(newSubCategory, SubCategoryDTO.class);
	}

	@Override
	public void deleteSubCategory(String subCategoryId) {
		SubCategory subCategory = this.subCategoryRepo.findById(subCategoryId)
				.orElseThrow(() -> new ResourceNotFoundException("SubCategory Not Exist With ID: "+subCategoryId));
		
		String fullPath = path + subCategory.getImageName();

		Path file = Paths.get(fullPath);
		try {
			Files.delete(file);	
		}catch (IOException e) {
			throw new ApiException("File Not Exist!! ");
		}
		
		this.subCategoryRepo.delete(subCategory);
	}

	@Override
	public SubCategoryDTO getSubCategoryById(String subCategoryId) {
		SubCategory subCategory = this.subCategoryRepo.findById(subCategoryId)
				.orElseThrow(() -> new ResourceNotFoundException("SubCategory Not Exist With ID: "+subCategoryId));
		return this.modelMapper.map(subCategory, SubCategoryDTO.class);
	}

	@Override
	public List<SubCategoryDTO> searchSubCategory(String keyword) {
		List<SubCategory> subCategories = this.subCategoryRepo.findBySubCategoryNameContainingIgnoreCase(keyword);
		List<SubCategoryDTO> dto = subCategories.stream().map(cat -> this.modelMapper.map(cat, SubCategoryDTO.class))
				.collect(Collectors.toList());
		return dto;
	}

	@Override
	public PageableResponse<SubCategoryDTO> getAllSubCategory(Integer pageNumber, Integer pageSize, String sortBy,
			String sortDir) {
		Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());
		Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
		Page<SubCategory> pages = this.subCategoryRepo.findAll(pageable);
		PageableResponse<SubCategoryDTO> response = Helper.getPageableResponse(pages, SubCategoryDTO.class);
		return response;
	}
	

	public String generateRandomId() {
		String randomID = UUID.randomUUID().toString();
		randomID = randomID.substring(0, 8).toUpperCase();
		return randomID;
	}
}
