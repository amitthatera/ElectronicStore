package com.estore;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.estore.entities.Roles;
import com.estore.repository.RolesRepository;
import com.estore.utility.AppConstant;

@SpringBootApplication
public class ElectronicsStoreApplication implements CommandLineRunner {
	
	@Autowired
	private RolesRepository roleRepo;

	public static void main(String[] args) {
		SpringApplication.run(ElectronicsStoreApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		
		Roles role1 = Roles.builder()
				.roleId(AppConstant.ADMIN)
				.roleName("ROLE_ADMIN")
				.build();
		
		Roles role2 = Roles.builder()
				.roleId(AppConstant.NORMAL)
				.roleName("ROLE_NORMAL")
				.build();
		
		this.roleRepo.saveAll(List.of(role1, role2));
	}

}
