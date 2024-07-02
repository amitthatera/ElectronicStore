package com.estore.entities;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;

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
public class Roles {

	@Id
	private long roleId;
	private String roleName;
	
	@Builder.Default
	@ManyToMany(mappedBy = "roles")
	private Set<User> users = new HashSet<>();
}
