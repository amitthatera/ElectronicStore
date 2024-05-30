package com.estore.entities;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
public class SubCategory {

	@Id
	private String subCategoryId;

	@Column(nullable = false, unique = true)
	private String subCategoryName;

	@Column(length = 10000)
	private String subCategoryDesc;

	private String imageName;

	@ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
	@JoinColumn(name = "category_id", referencedColumnName = "categoryId")
	private Category category;

	@Builder.Default
	@OneToMany(mappedBy = "subCategory", fetch = FetchType.EAGER)
	private Set<Product> products = new HashSet<>();
}
