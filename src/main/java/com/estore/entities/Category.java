package com.estore.entities;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

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
@Entity
public class Category {

	@Id
	private String categoryId;
	
	@Column(nullable = false, unique = true)
	private String categoryName;
	
	@Column(length = 10000)
	private String categoryDesc;
	
	@Builder.Default
	@OneToMany(mappedBy = "category", fetch = FetchType.EAGER)
	private Set<SubCategory> subCategory = new HashSet<>();
}
