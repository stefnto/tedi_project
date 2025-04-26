package com.example.backend.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.example.backend.filters.AuthenticationFilter;
import com.example.backend.services.JwtTokenServiceImpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

	@Value("${frontend.app.url}")
	private String frontendAppUrl;

	@Value("${backend.api.jwt.secret-key}")
	private String secretKey;

	private final JwtTokenServiceImpl jwtTokenService;

	/**
	 * Configures the security rules for HTTP requests on application start
	 */
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationConfiguration authenticationConfiguration) throws Exception {
		AuthenticationFilter authenticationFilter = new AuthenticationFilter(jwtTokenService, authenticationManager(authenticationConfiguration));
		authenticationFilter.setFilterProcessesUrl("/api/login");

		http
			.cors(cors -> cors.configurationSource(corsConfigurationSource())) // Use CorsConfigurationSource
			.csrf(csrf -> csrf.disable())
			.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.addFilter(authenticationFilter)
			.authorizeHttpRequests(authorize -> authorize
				.requestMatchers("/api/login", "/api/token/refresh", "/api/register").permitAll()
				.requestMatchers("/api/members").hasAuthority("SCOPE_ROLE_ADMIN") // Spring Security adds "SCOPE_" prefix to Granted Authorities when running oauth2ResourceServer with defaults - Customizer.withDefaults()
				.requestMatchers("/api/members/get/**", "/api/members/resume/**", "/api/friends/**", "/api/post/**", "/api/chatroom/**").hasAnyAuthority("SCOPE_ROLE_MEMBER", "SCOPE_ROLE_ADMIN")
				.anyRequest().authenticated()
			 )
			 .exceptionHandling(exception -> exception
				.accessDeniedHandler(customAccessDeniedHandler()) // Custom handler for 403 errors
			)
			.oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults())); // Enable JWT authentication using Spring Security's oauth2ResourceServer

		log.info("SecurityFilterChain initialized");

		return http.build();
	}

	@Bean
	public AccessDeniedHandler customAccessDeniedHandler() {
		return (request, response, accessDeniedException) -> {
			response.setStatus(HttpStatus.FORBIDDEN.value());
			response.getWriter().write(accessDeniedException.getMessage());
		};
	}

	/** 
	 * Called whenever spring security needs to handle an authentication request 
	 */
	@Bean
	AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}

	/**
	 *  Configures CORS to allow requests from any origin
	 */
	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration config = new CorsConfiguration();
		config.addAllowedOriginPattern(frontendAppUrl + ":*"); // Allow your frontend's origin
		config.addAllowedHeader("*");
		config.addAllowedMethod("*");
		config.setAllowCredentials(true); // Allow cookies or Authorization headers
		config.addExposedHeader("Authorization");
		config.addExposedHeader("Content-Type");

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", config);

		log.info("CORS configuration initialized");

		return source;
	}

	/**
	 * Configures Spring Security to read 'authorities' from the JWT token instead of default 'scope' and convert them to Granted Authorities.
	 */
	@Bean
  JwtAuthenticationConverter jwtAuthenticationConverter() {

    JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
    grantedAuthoritiesConverter.setAuthoritiesClaimName("authorities");

    JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
    jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);

    return jwtAuthenticationConverter;
  }

}
