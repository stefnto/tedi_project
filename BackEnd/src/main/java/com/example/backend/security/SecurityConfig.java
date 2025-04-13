package com.example.backend.security;

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
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import com.example.backend.filters.AuthenticationFilter;
import com.example.backend.filters.AuthorizationFilter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    /* Configures the security rules for HTTP requests on application start */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationConfiguration authenticationConfiguration) throws Exception {
        AuthenticationFilter authenticationFilter = new AuthenticationFilter(authenticationManager(authenticationConfiguration));
        authenticationFilter.setFilterProcessesUrl("/api/login");

        http
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

    /* Configures the authentication manager with the user details service and password encoder */
    // @Bean
    // public AuthenticationManagerBuilder configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
    //     auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
    //     return auth;
    // }

    /* Called whenever spring security needs to handle an authentication request */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    /* Configures CORS (Cross-Origin Resource Sharing) to allow requests from any origin */
    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();

        config.addAllowedOrigin("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        config.addExposedHeader("Authorization");
        config.addExposedHeader("Content-Type");

        source.registerCorsConfiguration("/**", config);

        log.info("Cors Filter initialized");

        return new CorsFilter(source);
    }
}
