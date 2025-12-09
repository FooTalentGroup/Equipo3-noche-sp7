package com.stockia.stockia.services.Impl;

import com.stockia.stockia.dtos.auth.*;
import com.stockia.stockia.enums.AccountStatus;
import com.stockia.stockia.enums.TokenPurpose;
import com.stockia.stockia.exceptions.*;
import com.stockia.stockia.mappers.UserMapper;
import com.stockia.stockia.models.User;
import com.stockia.stockia.repositories.UserRepository;
import com.stockia.stockia.security.CustomUserDetails;
import com.stockia.stockia.security.service.JwtService;
import com.stockia.stockia.security.service.TokenBlacklistService;
import com.stockia.stockia.services.AuthService;
import com.stockia.stockia.services.EmailService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    // --- Repositories ---
    private final UserRepository userRepository;

    // --- Security & Auth ---
    private final UserDetailsService userDetailsService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final TokenBlacklistService tokenBlacklistService;

    // --- Services ---
    private final EmailService emailService;

    // --- Mappers ---
    private final UserMapper userMapper;

    // --- Configuration values ---
    @Value("${frontend.baseUrl}")
    private String baseUrl;
    @Value("${frontend.resetPasswordUrl}")
    private String resetPasswordUrl;

    /**
     * Register a new user.
     */
    @Override
    @Transactional
    public RegisterResponseDto register(RegisterRequestDto requestDto) {
        if (userRepository.existsByEmail(requestDto.email())) {
            throw new DuplicateResourceException("El email '" + requestDto.email() + "' ya está registrado");
        }
        String encodedPassword = passwordEncoder.encode(requestDto.password());

        User user = userMapper.toUser(requestDto, encodedPassword);

        User savedUser = userRepository.save(user);

        return userMapper.toDto(savedUser);
    }

    /**
     * Performs authentication and generates JWT for the user.
     */
    @Override
    public LoginResponseDto login(LoginRequestDto requestDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(requestDto.email(), requestDto.password()));

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        User user = userDetails.getUser();

        if (user.getAccountStatus() != AccountStatus.ACTIVE) {
            throw new UnauthorizedException(
                    "Acceso denegado. Estado del usuario: " + user.getAccountStatus());
        }

        String token = jwtService.generateToken(userMapper.toJwtDataDto(user), TokenPurpose.AUTHENTICATION);
        return new LoginResponseDto(token);
    }

    /**
     * Add the token to the blacklist to log out.
     */
    @Override
    public void logout(String token) {
        try {
            LocalDateTime expiration = jwtService.extractExpiration(token);
            tokenBlacklistService.addTokenToBlacklist(token, expiration);
        } catch (Exception e) {
            throw new UnauthorizedException("Acceso no autorizado. Token inválido.");
        }
    }

    /**
     * Change the password, validating the current password
     * Generate a new authentication token.
     */
    @Override
    public void changePassword(ChangePasswordRequestDto requestDto) {
        User currentUser = getAuthenticatedUser()
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado"));

        if (!passwordEncoder.matches(requestDto.currentPassword(), currentUser.getPassword())) {
            throw new InvalidPasswordException("La contraseña actual es incorrecta");
        }

        currentUser.setPassword(passwordEncoder.encode(requestDto.newPassword()));
        userRepository.save(currentUser);
    }

    /**
     * Start the password recovery process by sending an email with a token.
     */
    @Override
    public void forgotPassword(ForgotPasswordRequestDto requestDto) {
        User user = userRepository.findByEmail(requestDto.email())
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado"));

        String token = jwtService.generateToken(userMapper.toJwtDataDto(user), TokenPurpose.RESET_PASSWORD);
        sendPasswordResetEmail(user, token);
    }

    /**
     * Reset the password by verifying the token and update it in the database.
     */
    @Transactional
    @Override
    public void resetPassword(ResetPasswordRequestDto requestDto) {
        TokenPurpose purpose = jwtService.extractPurpose(requestDto.token());
        if (!TokenPurpose.RESET_PASSWORD.equals(purpose)) {
            throw new InvalidTokenException("El token no es válido para recuperar contraseña");
        }

        String email = jwtService.extractEmail(requestDto.token());
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado"));

        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        if (!jwtService.isTokenValid(requestDto.token(), userDetails)) {
            throw new InvalidTokenException("Token inválido o expirado");
        }

        user.setPassword(passwordEncoder.encode(requestDto.newPassword()));
        userRepository.save(user);

        tokenBlacklistService.addTokenToBlacklist(requestDto.token(), jwtService.extractExpiration(requestDto.token()));
    }

    /**
     * Gets the currently authenticated user.
     */
    public Optional<User> getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return Optional.empty();
        }
        String username = authentication.getName();
        return userRepository.findByEmail(username);
    }

    // --------------------------------------------
    // Métodos privados auxiliares
    // --------------------------------------------

    /**
     * Envía email para recuperación de contraseña.
     */
    private void sendPasswordResetEmail(User user, String token) {
        Map<String, Object> templateModel = Map.of(
                "name", user.getName(),
                "recoveryPasswordUrl", baseUrl + resetPasswordUrl + "?token=" + token);

        emailService.sendHtmlEmail(
                user.getEmail(),
                "Recuperación de contraseña",
                "password-recovery",
                templateModel);
    }

}
