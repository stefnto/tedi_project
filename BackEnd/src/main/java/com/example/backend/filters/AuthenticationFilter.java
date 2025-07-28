package com.example.backend.filters;

import java.io.IOException;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.backend.models.LoginRequest;
import com.example.backend.services.JwtTokenServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	private final JwtTokenServiceImpl jwtTokenService;
	private final AuthenticationManager authenticationManager;

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

		 try {

        LoginRequest loginRequest = new ObjectMapper().readValue(request.getInputStream(), LoginRequest.class);

        String email = loginRequest.getEmail();
        String password = loginRequest.getPassword();

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(email, password);

        return authenticationManager.authenticate(authenticationToken);

    } catch (IOException e) {
        throw new RuntimeException("Failed to parse authentication request body", e);
    }
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException {
		User member = (User)authentication.getPrincipal();

		Map<String, String> tokenPair = jwtTokenService.generateAccessRefreshTokenPair(member);

		// Create an access token cookie
		Cookie accessTokenCookie = new Cookie("accessToken", tokenPair.get("accessToken"));
		accessTokenCookie.setHttpOnly(true);
    // accessTokenCookie.setSecure(true); // Only if you're using HTTPS (required in production)
    accessTokenCookie.setPath("/");
    accessTokenCookie.setMaxAge(60 * 30); // 30 minutes
		
		// Create a refresh token cookie
		Cookie refreshTokenCookie = new Cookie("refreshToken", tokenPair.get("refreshToken"));
		refreshTokenCookie.setHttpOnly(true);
    // refreshTokenCookie.setSecure(true); // Only if you're using HTTPS (required in production)
		refreshTokenCookie.setPath("/");
		refreshTokenCookie.setMaxAge(60 * 300); // 300 minutes

		// Add cookies to the response
		response.addCookie(accessTokenCookie);
		response.addCookie(refreshTokenCookie);

		// Generate the response body with the role
		Map<String, Object> responseBody = new java.util.HashMap<>();
    responseBody.put("role", member.getAuthorities().stream()
			.findFirst()
			.map(Object::toString)
			.orElse(null));
		
		new ObjectMapper().writeValue(response.getOutputStream(), responseBody);
	}

	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException {

		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		response.setContentType(APPLICATION_JSON_VALUE);

		new ObjectMapper().writeValue(response.getOutputStream(), Map.of("error", "Invalid email or password"));
	}

}
