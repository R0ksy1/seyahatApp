package com.roksy.seyahatuygulamasi.service;

import com.roksy.seyahatuygulamasi.common.dto.LoginDto;
import com.roksy.seyahatuygulamasi.common.dto.RegisterDto;

public interface IAuthService {
    String register(RegisterDto registerDto);
    String login(LoginDto loginDto);
}
