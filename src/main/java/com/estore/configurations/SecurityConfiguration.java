package com.estore.configurations;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.estore.jwt_utils.JwtAuthenticationEntryPoint;
import com.estore.jwt_utils.JwtAuthenticationFilter;



@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@EnableWebMvc
public class SecurityConfiguration {

	@Autowired
	private final JwtAuthenticationEntryPoint authenticationEntryPoint;

	@Autowired
	private final JwtAuthenticationFilter authenticationFilter;

	@Autowired
	private final AuthenticationProvider authenticationProvider;

	public SecurityConfiguration(AuthenticationProvider authenticationProvider,
						  JwtAuthenticationFilter authenticationFilter,
						  JwtAuthenticationEntryPoint authenticationEntryPoint) {
		this.authenticationProvider = authenticationProvider;
		this.authenticationFilter = authenticationFilter;
		this.authenticationEntryPoint = authenticationEntryPoint;
	}

	public static final String[] PUBLIC_URL = { 
			"/api/auth-user/**", 
			"/swagger-ui/**", 
			"/webjars/**",
			"/swagger-resources/**", 
			"/v3/api-docs/**" };

	@Bean
	SecurityFilterChain getHttpSecurity(HttpSecurity http) throws Exception {
			http
					.csrf(AbstractHttpConfigurer::disable)
					.authorizeHttpRequests(authorize -> authorize
							.requestMatchers(PUBLIC_URL).permitAll()
							.requestMatchers(HttpMethod.GET).permitAll()
							.anyRequest().authenticated()
					)
					.exceptionHandling(exceptionHandling ->
							exceptionHandling.authenticationEntryPoint(authenticationEntryPoint))
					.sessionManagement(sessionManagement ->
							sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
					.authenticationProvider(authenticationProvider)
					.addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class);

			return http.build();
	}

	@Bean
	FilterRegistrationBean<CorsFilter> corsFilter() {

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowCredentials(true);
		configuration.setAllowedOrigins(Arrays.asList("http://localhost:4200", "http://localhost:3000"));
		configuration.addAllowedHeader("Authorization");
		configuration.addAllowedHeader("Content-Type");
		configuration.addAllowedHeader("Accept");
		configuration.addAllowedMethod("GET");
		configuration.addAllowedMethod("POST");
		configuration.addAllowedMethod("DELETE");
		configuration.addAllowedMethod("PUT");
		configuration.addAllowedMethod("OPTIONS");
		configuration.setMaxAge(3600L);
		
		source.registerCorsConfiguration("/**", configuration);

		FilterRegistrationBean<CorsFilter> filterRegistrationBean = new FilterRegistrationBean<>(new CorsFilter(source));
		filterRegistrationBean.setOrder(-110);
		return filterRegistrationBean;
	}
}
