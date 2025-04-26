package com.example.backend.services;

import java.util.Map;

import org.springframework.security.core.userdetails.User;

public interface JwtTokenService {

  public Map<String, String> generateAccessRefreshTokenPair(User userDetails);

  public Map<String, String> generateAccessRefreshTokenPair(String refreshToken);
  
}
