package com.estore.configurations;

import java.util.Arrays;

import jakarta.servlet.http.HttpServletResponse;
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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfiguration {

	private final AuthenticationProvider authenticationProvider;

	public SecurityConfiguration(AuthenticationProvider authenticationProvider) {
		this.authenticationProvider = authenticationProvider;
	}

	public static final String[] PUBLIC_URL = {
			"/auth/**",
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
					.cors(cors -> cors.configurationSource(corsConfigurationSource()))
					.csrf(AbstractHttpConfigurer::disable)
					.authorizeHttpRequests(authorize -> authorize
							.requestMatchers(PUBLIC_URL).permitAll()
							.requestMatchers(HttpMethod.GET).permitAll()
							.anyRequest().authenticated()
					)
					.exceptionHandling(exceptionHandling -> exceptionHandling.authenticationEntryPoint((request, response, authException) ->
                            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.getMessage())))
					.sessionManagement(sessionManagement ->
							sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
					.authenticationProvider(authenticationProvider);


			return http.build();
	}

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
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
		return source;
	}
}
