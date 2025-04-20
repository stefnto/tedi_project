package com.example.backend.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.example.backend.filters.AuthenticationFilter;
import com.example.backend.filters.AuthorizationFilter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    @Value("${frontend.app.url}")
    private String frontendAppUrl;

    /* Configures the security rules for HTTP requests on application start */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationConfiguration authenticationConfiguration) throws Exception {
        AuthenticationFilter authenticationFilter = new AuthenticationFilter(authenticationManager(authenticationConfiguration));
        authenticationFilter.setFilterProcessesUrl("/api/login");

        http
            .cors(cors -> cors.configurationSource(corsConfigurationSource())) // Use CorsConfigurationSource
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(authorize -> 
                authorize
                    .requestMatchers("/api/login", "/api/token/refresh", "/api/register").permitAll()
                    .requestMatchers("/api/members").hasAuthority("ROLE_ADMIN")
                    .requestMatchers("/api/members/get/**", "/api/members/resume/**", "/api/friends/**", "/api/post/**", "/api/chatroom/**")
                    .hasAnyAuthority("ROLE_MEMBER", "ROLE_ADMIN")
                    .anyRequest().authenticated()
            )
            .addFilter(authenticationFilter)
            .addFilterBefore(new AuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);

        log.info("SecurityFilterChain initialized");

        return http.build();
    }

    /* Called whenever spring security needs to handle an authentication request */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    /* Configures CORS (Cross-Origin Resource Sharing) to allow requests from any origin */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
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
}
