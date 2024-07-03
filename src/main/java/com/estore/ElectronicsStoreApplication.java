package com.estore;

import com.estore.service_impl.RolesInitializationService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ElectronicsStoreApplication implements CommandLineRunner {

	private final RolesInitializationService rolesInitializationService;

	public ElectronicsStoreApplication(RolesInitializationService rolesInitializationService) {
		this.rolesInitializationService = rolesInitializationService;
	}

	public static void main(String[] args) {
		SpringApplication.run(ElectronicsStoreApplication.class, args);
	}

	@Override
	public void run(String... args) {
		rolesInitializationService.initializeRoles();
	}

}
