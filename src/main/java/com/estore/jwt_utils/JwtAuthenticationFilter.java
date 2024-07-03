package com.estore.jwt_utils;

import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;

import org.springframework.security.core.userdetails.UserDetailsService;

@NoArgsConstructor(force = true)
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final JwtUtils jwtUtils;

	private final UserDetailsService userService;

	private final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

	String jwtToken = null;
	String username = null;

	@Override
	protected void doFilterInternal(
			@NonNull HttpServletRequest request,
			@NonNull HttpServletResponse response,
			@NonNull FilterChain filterChain)
			throws ServletException, IOException {

		if (request.getServletPath().contains("/api/v1/auth")) {
			filterChain.doFilter(request, response);
			return;
		}

		final String authHeader = request.getHeader("Authorization");

		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			logger.error("Invalid Header !! {}", authHeader);
			filterChain.doFilter(request, response);
			return;
		}
		
		jwtToken = authHeader.substring(7);
		
		try {
            if (jwtUtils != null) {
                username = jwtUtils.extractUsername(jwtToken);
            }
        }catch (NullPointerException e){
			logger.error("Getting null value !!");
		}catch(IllegalArgumentException e) {
			logger.error("Illegal argument while fetching username !!");
		}catch (ExpiredJwtException e) {
			logger.error("Given JWT Token has expired !!");
		}catch (MalformedJwtException e) {
			logger.error("Some changed has done in token !!");
		}catch(Exception e) {
			logger.error(e.getMessage());
		}

		if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = null;
            if (userService != null) {
                userDetails = userService.loadUserByUsername(username);
            }
            if (userDetails != null && jwtUtils != null && jwtUtils.isTokenValid(jwtToken, userDetails)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails,
                        null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
		filterChain.doFilter(request, response);
	}
	
	public String getCurrentUser() {
		return username;
	}

}
