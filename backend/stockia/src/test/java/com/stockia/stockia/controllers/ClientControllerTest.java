package com.stockia.stockia.controllers;

import com.stockia.stockia.dtos.order.OrderResponseDto;
import com.stockia.stockia.exceptions.client.ClientNotFoundException;
import com.stockia.stockia.services.ClientService;
import com.stockia.stockia.utils.ApiResult;
import com.stockia.stockia.enums.OrderStatus;
import com.stockia.stockia.security.service.JwtService;
import com.stockia.stockia.security.service.TokenBlacklistService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@WebMvcTest(ClientController.class)
@DisplayName("ClientController - Purchase History Tests")
class ClientControllerPurchaseHistoryTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ClientService clientService;

    //  MOCKS PARA LA SEGURIDAD
    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private TokenBlacklistService tokenBlacklistService;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String ENDPOINT = "/api/clients/{clientId}/purchase-history";

    @Test
    @DisplayName("GET /api/clients/{id}/purchase-history - Debería retornar historial cuando cliente tiene órdenes")
    @WithMockUser(roles = "ADMIN")
    void getClientPurchaseHistory_ShouldReturnHistoryWhenClientHasOrders() throws Exception {
        // Cliente con 2 órdenes
        UUID clientId = UUID.randomUUID();
        List<OrderResponseDto> purchaseHistory = Arrays.asList(
            createOrderDto("ORD-20231124-0001", "Juan Pérez", BigDecimal.valueOf(450.00)),
            createOrderDto("ORD-20231123-0002", "Juan Pérez", BigDecimal.valueOf(320.50))
        );

        when(clientService.getClientPurchaseHistory(clientId)).thenReturn(purchaseHistory);

        //  Verificar respuesta exitosa
        mockMvc.perform(get(ENDPOINT, clientId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Se encontraron 2 compra(s) para el cliente"))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data", hasSize(2)))
                .andExpect(jsonPath("$.data[0].orderNumber").value("ORD-20231124-0001"))
                .andExpect(jsonPath("$.data[0].customerName").value("Juan Pérez"))
                .andExpect(jsonPath("$.data[0].totalAmount").value(450.00))
                .andExpect(jsonPath("$.data[1].orderNumber").value("ORD-20231123-0002"))
                .andExpect(jsonPath("$.data[1].totalAmount").value(320.50));
    }

    @Test
    @DisplayName("GET /api/clients/{id}/purchase-history - Debería retornar lista vacía cuando cliente no tiene órdenes")
    @WithMockUser(roles = "ADMIN")
    void getClientPurchaseHistory_ShouldReturnEmptyListWhenNoOrders() throws Exception {
        //  Cliente sin órdenes
        UUID clientId = UUID.randomUUID();
        List<OrderResponseDto> emptyHistory = Collections.emptyList();

        when(clientService.getClientPurchaseHistory(clientId)).thenReturn(emptyHistory);

        //  Verificar respuesta vacía pero exitosa
        mockMvc.perform(get(ENDPOINT, clientId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("El cliente no registra compras"))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data", hasSize(0)));
    }

    @Test
    @DisplayName("GET /api/clients/{id}/purchase-history - Debería retornar 404 cuando cliente no existe")
    @WithMockUser(roles = "ADMIN")
    void getClientPurchaseHistory_ShouldReturn404WhenClientNotFound() throws Exception {
        // Cliente no existe
        UUID clientId = UUID.randomUUID();
        when(clientService.getClientPurchaseHistory(clientId))
            .thenThrow(new ClientNotFoundException(clientId));

        // Verificar error 404
        mockMvc.perform(get(ENDPOINT, clientId))
                .andExpect(status().isNotFound());
    }

    /*@Test
    @DisplayName("GET /api/clients/{id}/purchase-history - Debería retornar 403 cuando usuario sin permisos")
    @WithMockUser(roles = "USER")
    void getClientPurchaseHistory_ShouldReturn403WhenInsufficientPermissions() throws Exception {
        //  Usuario sin rol ADMIN o MANAGER
        UUID clientId = UUID.randomUUID();

        //  Verificar acceso denegado
        mockMvc.perform(get(ENDPOINT, clientId))
                .andExpect(status().isForbidden());
    } */

    @Test
    @DisplayName("GET /api/clients/{id}/purchase-history - Debería retornar 401 cuando no autenticado")
    void getClientPurchaseHistory_ShouldReturn401WhenNotAuthenticated() throws Exception {
        //  Usuario no autenticado
        UUID clientId = UUID.randomUUID();

        // Verificar no autorizado
        mockMvc.perform(get(ENDPOINT, clientId))
                .andExpect(status().isUnauthorized());
    }

    /**
     * Método auxiliar para crear OrderResponseDto de prueba
     */
    private OrderResponseDto createOrderDto(String orderNumber, String customerName, BigDecimal totalAmount) {
        OrderResponseDto dto = new OrderResponseDto();
        dto.setId(UUID.randomUUID());
        dto.setOrderNumber(orderNumber);
        dto.setCustomerName(customerName);
        dto.setTotalAmount(totalAmount);
        dto.setOrderDate(LocalDateTime.now());
        dto.setStatus(OrderStatus.DELIVERED);
        return dto;
    }
}