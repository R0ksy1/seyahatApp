package com.roksy.seyahatuygulamasi.service;

import org.springframework.security.core.userdetails.UserDetails;
import java.util.function.Function;
import io.jsonwebtoken.Claims;
import java.util.Map;


public interface IJwtService {
    String generateToken(String username); // Metod adını değiştir
    String generateToken(Map<String, Object> extraClaims, String username); // Overload metod
    boolean isTokenValid(String token, UserDetails userDetails);
    String extractUsername(String token);
    <T> T extractClaim(String token, Function<Claims, T> claimsResolver);
}

