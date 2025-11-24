package com.stockia.stockia.services;

import com.stockia.stockia.dtos.auth.*;
import com.stockia.stockia.models.User;
import jakarta.transaction.Transactional;

import java.util.Optional;

public interface AuthService {
    RegisterResponseDto register(RegisterRequestDto requestDto);
    LoginResponseDto login(LoginRequestDto requestDto);
    void logout(String token);
    void changePassword(ChangePasswordRequestDto requestDto);
    void forgotPassword(ForgotPasswordRequestDto requestDto);
    void resetPassword(ResetPasswordRequestDto requestDto);
    Optional<User> getAuthenticatedUser();
}
