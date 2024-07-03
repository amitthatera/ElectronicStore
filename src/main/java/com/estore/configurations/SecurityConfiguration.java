package com.estore.configurations;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
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

import com.estore.jwt_utils.JwtAuthenticationEntryPoint;
import com.estore.jwt_utils.JwtAuthenticationFilter;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
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
			"/api/v1/auth/**",
			"/v3/api-docs",
			"/v3/api-docs/**",
			"/configuration/ui",
			"/configuration/security",
			"/swagger-ui/**",
			"/webjars/**",
			"/swagger-resources",
			"/swagger-resources/**",
			"/swagger-ui.html"
			};

	@Bean
	SecurityFilterChain getHttpSecurity(HttpSecurity http) throws Exception {
			http
					.cors(Customizer.withDefaults())
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
	@Order(Ordered.HIGHEST_PRECEDENCE)
	public CorsFilter corsFilter() {
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
		return new CorsFilter(source);
	}
}
