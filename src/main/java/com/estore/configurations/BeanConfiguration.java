package com.estore.configurations;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.estore.jwt_utils.JwtAuthenticationFilter;
import com.estore.repository.UserRepositiry;

@Configuration
public class BeanConfiguration {

	@Autowired
	private UserRepositiry userRepo;

	@Bean
	ModelMapper getModelMapper() {
		return new ModelMapper();
	}

	@Bean
	UserDetailsService getUserDetailsService() {
		return username -> this.userRepo.findByEmailAddressIgnoreCase(username)
				.orElseThrow(() -> new UsernameNotFoundException("User Not Exist !!"));
	}

	@Bean
	JwtAuthenticationFilter getAuthenticationFilter() {
		return new JwtAuthenticationFilter();
	}
	
	@Bean
    BCryptPasswordEncoder getPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	AuthenticationProvider getAuthenticationProvider() {
	   DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
	   authProvider.setUserDetailsService(getUserDetailsService());
	   authProvider.setPasswordEncoder(getPasswordEncoder());
	   return authProvider;
	}
	
	@Bean
	AuthenticationManager getAuthenticationManager(AuthenticationConfiguration configuration) throws Exception {
		return configuration.getAuthenticationManager();
	}
}
