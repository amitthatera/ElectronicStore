package com.estore.services;

import java.util.List;

import com.estore.dto.PageableResponse;
import com.estore.dto.ProductDTO;

public interface ProductServices {

	ProductDTO createProduct(ProductDTO productDto);

	ProductDTO createProduct(ProductDTO productDto, String subCategoryId);

	ProductDTO updateProduct(ProductDTO productDto, String productId);

	ProductDTO updateSubCategory(String productId, String subCategoryId);

	void deleteProduct(String productId);

	ProductDTO getByProductId(String productId);

	List<ProductDTO> searchProduct(String keyword);

	PageableResponse<ProductDTO> getBySubCategoryId(String subCategoryId, Integer pageNumber, Integer pageSize, String sortBy,
			String sortDir);

	PageableResponse<ProductDTO> getAllActiveProduct(Integer pageNumber, Integer pageSize, String sortBy,
			String sortDir);

	PageableResponse<ProductDTO> getAllProduct(Integer pageNumber, Integer pageSize, String sortBy, String sortDir);

}
