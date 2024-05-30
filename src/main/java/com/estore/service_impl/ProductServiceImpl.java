package com.estore.service_impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;
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
import com.estore.dto.ProductDTO;
import com.estore.entities.Images;
import com.estore.entities.Product;
import com.estore.entities.SubCategory;
import com.estore.repository.ProductRepository;
import com.estore.repository.SubCategoryRepository;
import com.estore.services.ProductServices;
import com.estore.utility.Helper;

@Service
public class ProductServiceImpl implements ProductServices {


	@Autowired
	private ProductRepository productRepo;

	@Autowired
	private SubCategoryRepository subCategoryRepo;

	@Autowired
	private ModelMapper modelMapper;

	@Value("${product.image.path}")
	private String path;

	@Override
	public ProductDTO createProduct(ProductDTO productDto) {
		Product product = this.modelMapper.map(productDto, Product.class);
		product.setProductId(generateRandomId());
		String discountPercentage = getDiscountPercentage(productDto.getActualPrice(), productDto.getDiscountedPrice());
		product.setDiscountPercentage(discountPercentage);
		Product newProduct = this.productRepo.save(product);
		return this.modelMapper.map(newProduct, ProductDTO.class);
	}

	@Override
	public ProductDTO createProduct(ProductDTO productDto, String subCategoryId) {
		Product product = this.modelMapper.map(productDto, Product.class);

		SubCategory subCategory = this.subCategoryRepo.findById(subCategoryId)
				.orElseThrow(() -> new ResourceNotFoundException("Category Not EXist With ID: " + subCategoryId));
		
		product.setSubCategory(subCategory);
		product.setProductId(generateRandomId());

		String discountPercentage = getDiscountPercentage(productDto.getActualPrice(), productDto.getDiscountedPrice());
		product.setDiscountPercentage(discountPercentage);

		Product newProduct = this.productRepo.save(product);
		return this.modelMapper.map(newProduct, ProductDTO.class);
	}

	@Override
	public ProductDTO updateProduct(ProductDTO productDto, String productId) {
		Product product = this.productRepo.findById(productId)
				.orElseThrow(() -> new ResourceNotFoundException("Product Not Exist With ID: " + productId));
		product.setProductName(productDto.getProductName());
		product.setActualPrice(productDto.getActualPrice());
		product.setDiscountedPrice(productDto.getDiscountedPrice());
		product.setProductDesc(productDto.getProductDesc());
		product.setUpdationDate(productDto.getUpdationDate());
		product.setIsActive(productDto.getIsActive());
		product.setInStock(productDto.getInStock());
		product.setProductStock(productDto.getProductStock());
		
		Set<Images> images = product.getProductImages();
		for (Images image : images) {
			String filePath = path + image.getImageName();
			try {
				Files.delete(Paths.get(filePath));
			} catch (IOException e) {
				throw new ApiException("File Not Exist");
			}
		}
		
		product.setProductImages(productDto.getProductImages());

		String discountPercentage = getDiscountPercentage(productDto.getActualPrice(), productDto.getDiscountedPrice());
		product.setDiscountPercentage(discountPercentage);

		Product updatedProduct = this.productRepo.save(product);
		return this.modelMapper.map(updatedProduct, ProductDTO.class);
	}

	@Override
	public ProductDTO updateSubCategory(String productId, String subCategoryId) {
		Product product = this.productRepo.findById(productId)
				.orElseThrow(() -> new ResourceNotFoundException("Product Not Exist With ID: " + productId));

		SubCategory category = this.subCategoryRepo.findById(subCategoryId)
				.orElseThrow(() -> new ResourceNotFoundException("Category Not Exist With ID: " + subCategoryId));
		product.setSubCategory(category);
		Product updatedProduct = this.productRepo.save(product);
		return this.modelMapper.map(updatedProduct, ProductDTO.class);
	}

	@Override
	public void deleteProduct(String productId) {
		Product product = this.productRepo.findById(productId)
				.orElseThrow(() -> new ResourceNotFoundException("Product Not Exist With ID: " + productId));
		Set<Images> images = product.getProductImages();
		for (Images image : images) {
			String filePath = path + image.getImageName();
			try {
				Files.delete(Paths.get(filePath));
			} catch (IOException e) {
				throw new ApiException("File Not Exist");
			}
		}
		this.productRepo.delete(product);
	}

	@Override
	public ProductDTO getByProductId(String productId) {
		Product product = this.productRepo.findById(productId)
				.orElseThrow(() -> new ResourceNotFoundException("Product Not Exist With ID: " + productId));
		return this.modelMapper.map(product, ProductDTO.class);
	}

	@Override
	public List<ProductDTO> searchProduct(String keyword) {
		List<Product> products = this.productRepo.findByProductNameContainingIgnoreCase(keyword);
		List<ProductDTO> productsDto = products.stream()
				.map(product -> this.modelMapper.map(product, ProductDTO.class)).collect(Collectors.toList());
		return productsDto;
	}

	@Override
	public PageableResponse<ProductDTO> getBySubCategoryId(String subCategoryId, Integer pageNumber, Integer pageSize,
			String sortBy, String sortDir) {
		SubCategory category = this.subCategoryRepo.findById(subCategoryId)
				.orElseThrow(() -> new ResourceNotFoundException("Category Not Exist With ID: " + subCategoryId));
		Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());
		Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
		Page<Product> page = this.productRepo.findBySubCategory(category, pageable);
		PageableResponse<ProductDTO> response = Helper.getPageableResponse(page, ProductDTO.class);
		return response;
	}

	@Override
	public PageableResponse<ProductDTO> getAllActiveProduct(Integer pageNumber, Integer pageSize, String sortBy,
			String sortDir) {
		Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());
		Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
		Page<Product> page = this.productRepo.findByisActiveTrue(pageable);
		PageableResponse<ProductDTO> response = Helper.getPageableResponse(page, ProductDTO.class);
		return response;
	}

	@Override
	public PageableResponse<ProductDTO> getAllProduct(Integer pageNumber, Integer pageSize, String sortBy,
			String sortDir) {
		Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());
		Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
		Page<Product> page = this.productRepo.findAll(pageable);
		PageableResponse<ProductDTO> response = Helper.getPageableResponse(page, ProductDTO.class);
		return response;
	}

	public String getDiscountPercentage(Double actualPrice, Double discountedPrice) {
		Double discount = actualPrice - discountedPrice;
		Double discountPercentage = (discount / actualPrice) * 100;
		Long formattedDiscount = Math.round(discountPercentage);
		return formattedDiscount + "%";
	}

	public String generateRandomId() {
		String randomID = UUID.randomUUID().toString();
		randomID = randomID.substring(0, 15).toUpperCase();
		return randomID;
	}
}
      