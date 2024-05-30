package com.estore.dto;

import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

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
public class CategoryDTO {

	private String categoryId;
	
	@NotBlank(message = "Name can not be blank !!")
	@Size(min = 3, message = "Name must contain 3 characters !!")
	private String categoryName;
	
    @Size(min = 100, message = "Description must be in 100 characters !!")
	private String categoryDesc;
	
	@Builder.Default
	@JsonIgnore
	private Set<SubCategoryDTO> subCategory = new HashSet<>();
}
