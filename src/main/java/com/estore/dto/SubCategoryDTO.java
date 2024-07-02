package com.estore.dto;

import java.util.HashSet;
import java.util.Set;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
public class SubCategoryDTO {

	private String subCategoryId;

	@NotBlank(message = "Name can not be blank !!")
	@Size(min = 3, message = "Name must contain 3 characters !!")
	private String subCategoryName;

	@Size(min = 100, message = "Description must be in 100 characters !!")
	private String subCategoryDesc;

	private String imageName;

	private CategoryDTO category;

	@Builder.Default
	@JsonIgnore
	private Set<ProductDTO> products = new HashSet<>();
}
