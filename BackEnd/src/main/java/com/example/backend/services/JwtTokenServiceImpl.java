package com.example.backend.services;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;

/**
 * Service class for generating and decoding JWT tokens
 */
@Service
@Slf4j
public class JwtTokenServiceImpl implements JwtTokenService {

  private final JwtEncoder jwtEncoder;
  private final JwtDecoder jwtDecoder;

  public JwtTokenServiceImpl(JwtEncoder jwtEncoder, JwtDecoder jwtDecoder) {
    this.jwtEncoder = jwtEncoder;
    this.jwtDecoder = jwtDecoder;
  }

  @Value("${backend.api.jwt.secret-key}")
  private String secretKey;

  @Value("${backend.app.url}")
  private String backendUrl;

  @Override
  public Map<String, String> generateAccessRefreshTokenPair(User userDetails) {

    Instant now = Instant.now();

    // Create a map to hold the access and refresh tokens
    Map<String, String> tokenPair = new HashMap<>();
    
    // Create access and refresh token claims
    JwtClaimsSet accessTokenClaims = JwtClaimsSet.builder()
      .subject(userDetails.getUsername())
      .claim("authorities", userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList())
      .issuer((backendUrl))
      .issuedAt(now)
      .expiresAt(now.plus(30, ChronoUnit.MINUTES))
      .build();

    JwtClaimsSet refreshTokenClaims = JwtClaimsSet.builder()
      .subject(userDetails.getUsername())
      .claim("authorities", userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList())
      .issuer((backendUrl))
      .issuedAt(now)
      .expiresAt(now.plus(300, ChronoUnit.MINUTES))
      .build();

    // Set the JWS header with the HS256 algorithm
    JwsHeader jwsHeader = JwsHeader.with(MacAlgorithm.HS256).build();

    // Encode the tokens using the JWT encoder and add them to the tokenPair map
    tokenPair.put("access_token", jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, accessTokenClaims)).getTokenValue());
    tokenPair.put("refresh_token", jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, refreshTokenClaims)).getTokenValue());

    return tokenPair;
  }
  
  @Override
  public Map<String, String> generateAccessRefreshTokenPair(String refreshToken) {

    Jwt decodedToken = jwtDecoder.decode(refreshToken);

    Instant now = Instant.now();

    // Create a map to hold the access and refresh tokens
    Map<String, String> tokenPair = new HashMap<>();

    log.info(decodedToken.getClaim("authorities").toString());

    // Create access and refresh token claims
    JwtClaimsSet accessTokenClaims = JwtClaimsSet.builder()
      .subject(decodedToken.getSubject())
      .claim("authorities", decodedToken.getClaim("authorities"))
      .issuer((backendUrl))
      .issuedAt(now)
      .expiresAt(now.plus(30, ChronoUnit.MINUTES))
      .build();

    JwtClaimsSet refreshTokenClaims = JwtClaimsSet.builder()
      .subject(decodedToken.getSubject())
      .claim("authorities", decodedToken.getClaim("authorities"))
      .issuer((backendUrl))
      .issuedAt(now)
      .expiresAt(now.plus(300, ChronoUnit.MINUTES))
      .build();

    // Set the JWS header with the HS256 algorithm
    JwsHeader jwsHeader = JwsHeader.with(MacAlgorithm.HS256).build();

    // Encode the tokens using the JWT encoder and add them to the tokenPair map
    tokenPair.put("access_token", jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, accessTokenClaims)).getTokenValue());
    tokenPair.put("refresh_token", jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, refreshTokenClaims)).getTokenValue());

    return tokenPair;
  }
}
