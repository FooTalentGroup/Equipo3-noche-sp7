package com.stockia.stockia.controllers;

import com.stockia.stockia.documentation.auth.*;
import com.stockia.stockia.dtos.auth.*;
import com.stockia.stockia.exceptions.UnauthorizedException;
import com.stockia.stockia.services.AuthService;
import com.stockia.stockia.utils.ApiResult;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "01 - Autenticación",
        description = "Endpoints para autenticación de usuarios y gestión de cuentas")
public class AuthController {
    private final AuthService authService;

    @RegisterEndpointDoc
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/register")
    public ResponseEntity<ApiResult<?>> register(@RequestBody @Valid RegisterRequestDto requestDto) {
        RegisterResponseDto response = authService.register(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResult.success(response, "Usuario registrado exitosamente."));
    }

    @LoginEndpointDoc
    @PostMapping("/login")
    public ResponseEntity<ApiResult<?>> login(@RequestBody @Valid LoginRequestDto requestDto) {
        LoginResponseDto response = authService.login(requestDto);
        return ResponseEntity.ok(ApiResult.success(response, "Login exitoso."));
    }

    @LogoutEndpointDoc
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/logout")
    public ResponseEntity<ApiResult<?>> logout(HttpServletRequest requestDto) {
        String authHeader = requestDto.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new UnauthorizedException("Acceso no autorizado. Token ausente.");
        }
        String token = authHeader.substring(7);
        authService.logout(token);
        return ResponseEntity.ok(ApiResult.success("Logout exitoso."));
    }

    @ChangePasswordEndpointDoc
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody @Valid ChangePasswordRequestDto requestDto) {
        authService.changePassword(requestDto);
        return ResponseEntity.ok(ApiResult.success("Contraseña cambiada correctamente"));
    }

    @ForgotPasswordEndpointDoc
    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody @Valid ForgotPasswordRequestDto requestDto) {
        authService.forgotPassword(requestDto);
        return ResponseEntity.ok(ApiResult.success("Se ha enviado un correo para restablecer tu contraseña"));
    }

    @ResetPasswordEndpointDoc
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody @Valid ResetPasswordRequestDto request) {
        authService.resetPassword(request);
        return ResponseEntity.ok(ApiResult.success("Contraseña restablecida correctamente"));
    }
}
