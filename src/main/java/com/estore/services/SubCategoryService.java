package com.estore.services;

import java.util.List;

import com.estore.dto.SubCategoryDTO;
import com.estore.dto.PageableResponse;

public interface SubCategoryService {

	SubCategoryDTO createSubCategory(SubCategoryDTO subCategoryDTO);
	
	SubCategoryDTO createSubCategory(SubCategoryDTO subCategoryDTO, String categoryId);

	SubCategoryDTO updateSubCategory(SubCategoryDTO subCategoryDTO, String subCategoryId);
	
	SubCategoryDTO updateCategory(String subCategoryId, String categoryId);

	void deleteSubCategory(String subCategoryId);

	SubCategoryDTO getSubCategoryById(String subCategoryId);

	List<SubCategoryDTO > searchSubCategory(String keyword);

	PageableResponse<SubCategoryDTO> getAllSubCategory(Integer pageNumber, Integer pageSize, String sortBy, String sortDir);

}
