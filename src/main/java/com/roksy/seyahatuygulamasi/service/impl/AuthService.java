package com.roksy.seyahatuygulamasi.service.impl;

import com.roksy.seyahatuygulamasi.common.dto.LoginDto;
import com.roksy.seyahatuygulamasi.common.dto.RegisterDto;
import com.roksy.seyahatuygulamasi.data.jpa.UserEntity;
import com.roksy.seyahatuygulamasi.data.repository.UserRepository;
import com.roksy.seyahatuygulamasi.service.IAuthService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService implements IAuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }
    @Override
    public String register(RegisterDto registerDto){
        if(userRepository.findByUsername(registerDto.username()).isPresent()){
            throw new IllegalArgumentException("Bu kullanici zaten var");
        }

        UserEntity newUser = new UserEntity();
        newUser.setUsername(registerDto.username());
        newUser.setEmail(registerDto.email());
        newUser.setPassword(passwordEncoder.encode(registerDto.password()));
        userRepository.save(newUser);
        return null;
    }

    @Override
    public String login(LoginDto loginDto) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDto.username(),
                        loginDto.password()
                )
        );


        var user = userRepository.findByUsername(loginDto.username())
                .orElseThrow(() -> new IllegalStateException("Kimlik doğrulama sonrası kullanıcı bulunamadı."));

        String jwtToken = jwtService.generateToken(user.getUsername());

        return jwtToken;
    }
}
