package com.stockia.stockia.controllers;

import com.stockia.stockia.dtos.auth.RegisterRequestDto;
import com.stockia.stockia.dtos.auth.RegisterResponseDto;
import com.stockia.stockia.enums.AccountStatus;
import com.stockia.stockia.enums.Role;
import com.stockia.stockia.security.service.JwtService;
import com.stockia.stockia.services.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.extension.ExtendWith;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.http.MediaType;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
public class AuthControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuthService authService;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private AuthenticationManager authenticationManager;

    private RegisterRequestDto testRegisterRequestDto;
    private RegisterResponseDto testReponseDto;

    @BeforeEach
    void setUp() {
        testRegisterRequestDto = new RegisterRequestDto(
                "test@example.com",
                "Password1@",
                "Test User",
                Role.MANAGER
        );

        testReponseDto = new RegisterResponseDto(
                UUID.randomUUID(),
                "test@example.com",
                "Test User",
                Role.MANAGER,
                AccountStatus.ACTIVE,
                false,
                null,
                null
                );
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void register_success() throws Exception {
        RegisterResponseDto mockResponse = new RegisterResponseDto(
                UUID.randomUUID(),
                "test@example.com",
                "Test User",
                Role.MANAGER,
                AccountStatus.ACTIVE,
                false,
                null,
                null
        );

        when(authService.register(any())).thenReturn(mockResponse);

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "email": "test@example.com",
                                    "password": "Password1@",
                                    "name": "Test User",
                                    "role": "MANAGER"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.email").value("test@example.com"))
                .andExpect(jsonPath("$.message").value("Usuario registrado exitosamente."));
    }
}
