package com.stockia.stockia.services;

import com.stockia.stockia.dtos.auth.*;
import com.stockia.stockia.enums.AccountStatus;
import com.stockia.stockia.enums.Role;
import com.stockia.stockia.enums.TokenPurpose;
import com.stockia.stockia.exceptions.DuplicateResourceException;
import com.stockia.stockia.mappers.UserMapper;
import com.stockia.stockia.models.User;
import com.stockia.stockia.repositories.UserRepository;
import com.stockia.stockia.security.CustomUserDetails;
import com.stockia.stockia.security.service.JwtService;
import com.stockia.stockia.services.Impl.AuthServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias para AuthServiceImplTest.
 * Valida toda la lógica de negocio del módulo de Autenticación.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("OrderService - Unit Tests")
class AuthServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtService jwtService;

    @Mock
    private UserMapper userMapper;

    @Mock
    private Authentication authentication;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private AuthServiceImpl authService;

    private User testUser;
    private RegisterRequestDto testRegisterRequestDto;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(UUID.randomUUID());
        testUser.setEmail("test@example.com");
        testUser.setName("Test User");
        testUser.setAccountStatus(AccountStatus.ACTIVE);

        testRegisterRequestDto = new RegisterRequestDto(
                "test@example.com",
                "Password1@",
                "Test User",
                Role.MANAGER
        );
    }

    @Test
    @DisplayName("Should create user successfully")
    void register_success() {

        when(userRepository.existsByEmail(testRegisterRequestDto.email())).thenReturn(false);
        when(userMapper.toUser(any(), anyString())).thenReturn(testUser);
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArgument(0));
        when(passwordEncoder.encode(testRegisterRequestDto.password())).thenReturn("encodedPassword");
        when(userMapper.toDto(testUser)).thenReturn(mock(RegisterResponseDto.class));

        RegisterResponseDto response = authService.register(testRegisterRequestDto);

        assertNotNull(response);
        verify(userRepository).save(any(User.class));
    }

    @Test
    void register_emailAlreadyExists_throwsException() {
        when(userRepository.existsByEmail(testRegisterRequestDto.email())).thenReturn(true);
        assertThrows(DuplicateResourceException.class, () -> authService.register(testRegisterRequestDto));
    }

    @Test
    void login_success() {
        LoginRequestDto requestDto = new LoginRequestDto("test@example.com", "Password1@");

        CustomUserDetails userDetails = mock(CustomUserDetails.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUser()).thenReturn(testUser);
        when(jwtService.generateToken(any(), any())).thenReturn("token123");

        LoginResponseDto response = authService.login(requestDto);
        assertEquals("token123", response.token());
    }

    @Test
    void changePassword_success() {
        Authentication auth = mock(Authentication.class);
        when(auth.isAuthenticated()).thenReturn(true);
        when(auth.getName()).thenReturn(testUser.getEmail());
        SecurityContextHolder.getContext().setAuthentication(auth);

        when(userRepository.findByEmail(testUser.getEmail())).thenReturn(Optional.of(testUser));
        testUser.setPassword("encodedPassword");
        when(passwordEncoder.matches("current", "encodedPassword")).thenReturn(true);
        when(passwordEncoder.encode("newPass")).thenReturn("newEncoded");

        authService.changePassword(new ChangePasswordRequestDto("current", "newPass"));

        assertEquals("newEncoded", testUser.getPassword());
        verify(userRepository).save(testUser);
    }

    @Test
    void forgotPassword_success() {
        ForgotPasswordRequestDto request = new ForgotPasswordRequestDto("test@example.com");

        when(userRepository.findByEmail(request.email())).thenReturn(Optional.of(testUser));
        when(jwtService.generateToken(any(), eq(TokenPurpose.RESET_PASSWORD))).thenReturn("resetToken");

        authService.forgotPassword(request);

        verify(jwtService).generateToken(any(), eq(TokenPurpose.RESET_PASSWORD));
        verify(emailService).sendHtmlEmail(
                eq(testUser.getEmail()),
                eq("Recuperación de contraseña"),
                eq("password-recovery"),
                anyMap()
        );
    }

}
