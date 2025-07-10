package com.roksy.seyahatuygulamasi.rest.controller;

import com.roksy.seyahatuygulamasi.common.dto.RegisterDto;
import com.roksy.seyahatuygulamasi.service.impl.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.roksy.seyahatuygulamasi.common.dto.LoginDto;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register") // Bu metot, /api/auth/register adresine gelen POST isteklerini karşılar
    public ResponseEntity<String> registerUser(@RequestBody RegisterDto registerDto) {
        authService.register(registerDto);
        return ResponseEntity.ok("Kullanıcı başarıyla kaydedildi!");
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> loginUser(@RequestBody LoginDto loginDto) {
        String token = authService.login(loginDto);

        Map<String, String> response = Map.of("token", token);

        return ResponseEntity.ok(response);
    }
}
