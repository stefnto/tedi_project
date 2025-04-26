package com.example.backend.controllers;

import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.services.JwtTokenServiceImpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/token")
public class TokenController {

  private final JwtTokenServiceImpl jwtTokenService;

  @Value("${backend.api.jwt.secret-key}")
  private String secretKey;

  @PostMapping("/refresh")
  public ResponseEntity<Map<String, String>> refreshToken(@RequestHeader("Authorization") String authorizationHeader) {

    if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid refresh token"));
    }

    try {
        // Extract the refresh token from the Authorization header
        String refreshToken = authorizationHeader.substring("Bearer ".length());

        // Return the new tokens
        Map<String, String> tokenPair = jwtTokenService.generateAccessRefreshTokenPair(refreshToken);

        return ResponseEntity.ok(tokenPair);

    } catch (JwtException e) {
        // Handle invalid or expired refresh token
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid or expired refresh token"));
    }
  }
  
}
