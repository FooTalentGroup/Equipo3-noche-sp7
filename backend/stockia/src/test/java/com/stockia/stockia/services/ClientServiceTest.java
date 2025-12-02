package com.stockia.stockia.services;

import com.stockia.stockia.models.Client;
import com.stockia.stockia.repositories.ClientRepository;
import com.stockia.stockia.exceptions.client.ClientDuplicatedException;
import com.stockia.stockia.exceptions.client.ClientNotFoundException;
import com.stockia.stockia.dtos.order.OrderResponseDto;
import com.stockia.stockia.repositories.OrderRepository;
import com.stockia.stockia.mappers.OrderMapper;
import com.stockia.stockia.models.Order;
import com.stockia.stockia.enums.OrderStatus;
import com.stockia.stockia.enums.PaymentMethod;
import com.stockia.stockia.enums.PaymentStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias para la capa de servicio ClientService.
 * 
 * Esta clase contiene tests que verifican la lógica de negocio
 * del registro de clientes y consulta de historial de compras.
 */
@ExtendWith(MockitoExtension.class)
class ClientServiceTest {

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderMapper orderMapper;

    @InjectMocks
    private ClientService clientService;

    // ========== TESTS EXISTENTES DE REGISTRO ==========

    @Test
    void shouldRegisterNewClientSuccessfully() {
        Client newClient = new Client();
        newClient.setName("Carlos López");
        newClient.setEmail("carlos@test.com");
        newClient.setPhone("+5491123456789");
        newClient.setIsFrequent(true);

        when(clientRepository.findByEmailOrPhone(anyString(), anyString()))
                .thenReturn(Optional.empty());
        when(clientRepository.save(any(Client.class)))
                .thenAnswer(invocation -> {
                    Client client = invocation.getArgument(0);
                    client.setId(UUID.randomUUID());
                    return client;
                });

        Client result = clientService.registerClient(newClient);

        assertThat(result.getPhone()).isEqualTo("+5491123456789");
        verify(clientRepository).save(any(Client.class));
    }

    @Test
    void shouldThrowExceptionWhenClientDuplicatedByPhone() {
        Client newClient = new Client();
        newClient.setName("Pedro Martínez");
        newClient.setEmail("pedro@nuevo.com");
        newClient.setPhone("+573123456789");
        newClient.setIsFrequent(true);

        Client existingClient = new Client();
        existingClient.setId(UUID.randomUUID());
        existingClient.setPhone("+573123456789");

        when(clientRepository.findByEmailOrPhone(anyString(), anyString()))
                .thenReturn(Optional.of(existingClient));

        assertThatThrownBy(() -> clientService.registerClient(newClient))
                .isInstanceOf(ClientDuplicatedException.class);
    }

    @Test
    void shouldThrowExceptionWhenClientDuplicatedByEmail() {
        Client newClient = new Client();
        newClient.setName("Ana García");
        newClient.setEmail("ana@duplicado.com");
        newClient.setPhone("+34612345678");
        newClient.setIsFrequent(false);

        Client existingClient = new Client();
        existingClient.setId(UUID.randomUUID());
        existingClient.setEmail("ana@duplicado.com");

        when(clientRepository.findByEmailOrPhone(anyString(), anyString()))
                .thenReturn(Optional.of(existingClient));

        assertThatThrownBy(() -> clientService.registerClient(newClient))
                .isInstanceOf(ClientDuplicatedException.class);

        verify(clientRepository, never()).save(any(Client.class));
    }

    // ========== NUEVOS TESTS DE HISTORIAL DE COMPRAS ==========

    @Test
    @DisplayName("Debería retornar historial cuando cliente existe con órdenes")
    void getClientPurchaseHistory_ShouldReturnOrdersWhenClientExists() {
        // Given: Cliente válido con 2 órdenes
        UUID clientId = UUID.randomUUID();
        Client client = createClient(clientId, "Juan Pérez", "juan@test.com");
        
        Order order1 = createOrder("ORD-001", client, BigDecimal.valueOf(450.00));
        Order order2 = createOrder("ORD-002", client, BigDecimal.valueOf(320.00));
        List<Order> orders = Arrays.asList(order1, order2);
        
        OrderResponseDto dto1 = createOrderDto("ORD-001", BigDecimal.valueOf(450.00));
        OrderResponseDto dto2 = createOrderDto("ORD-002", BigDecimal.valueOf(320.00));
        
        when(clientRepository.findById(clientId)).thenReturn(Optional.of(client));
        when(orderRepository.findByCustomerIdOrderByOrderDateDesc(clientId)).thenReturn(orders);
        when(orderMapper.toResponseDto(order1)).thenReturn(dto1);
        when(orderMapper.toResponseDto(order2)).thenReturn(dto2);

        // When: Llamar al servicio
        List<OrderResponseDto> result = clientService.getClientPurchaseHistory(clientId);

        // Then: Verificar resultado
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getOrderNumber()).isEqualTo("ORD-001");
        assertThat(result.get(1).getOrderNumber()).isEqualTo("ORD-002");
        
        verify(clientRepository).findById(clientId);
        verify(orderRepository).findByCustomerIdOrderByOrderDateDesc(clientId);
        verify(orderMapper, times(2)).toResponseDto(any(Order.class));
    }

    @Test
    @DisplayName("Debería retornar lista vacía cuando cliente no tiene órdenes")
    void getClientPurchaseHistory_ShouldReturnEmptyListWhenNoOrders() {
        // Given: Cliente válido sin órdenes
        UUID clientId = UUID.randomUUID();
        Client client = createClient(clientId, "María López", "maria@test.com");
        
        when(clientRepository.findById(clientId)).thenReturn(Optional.of(client));
        when(orderRepository.findByCustomerIdOrderByOrderDateDesc(clientId)).thenReturn(Collections.emptyList());

        // When
        List<OrderResponseDto> result = clientService.getClientPurchaseHistory(clientId);

        // Then: Lista vacía, sin llamar mapper
        assertThat(result).isEmpty();
        verify(orderMapper, never()).toResponseDto(any());
    }

    @Test
    @DisplayName("Debería lanzar excepción cuando cliente no existe")
    void getClientPurchaseHistory_ShouldThrowExceptionWhenClientNotFound() {
        // Given: Cliente no existe
        UUID clientId = UUID.randomUUID();
        when(clientRepository.findById(clientId)).thenReturn(Optional.empty());

        // When & Then: Excepción lanzada
        assertThatThrownBy(() -> clientService.getClientPurchaseHistory(clientId))
                .isInstanceOf(ClientNotFoundException.class)
                .hasMessageContaining(clientId.toString());
        
        verify(orderRepository, never()).findByCustomerIdOrderByOrderDateDesc(any());
    }

    // ========== MÉTODOS AUXILIARES ==========

    private Client createClient(UUID id, String name, String email) {
        Client client = new Client();
        client.setId(id);
        client.setName(name);
        client.setEmail(email);
        client.setPhone("+1234567890");
        client.setIsFrequent(false);
        return client;
    }

    private Order createOrder(String orderNumber, Client customer, BigDecimal total) {
        return Order.builder()
                .id(UUID.randomUUID())
                .orderNumber(orderNumber)
                .customer(customer) // ← CORREGIDO: usar .customer() en lugar de .customerId()
                .status(OrderStatus.DELIVERED)
                .totalAmount(total)
                .paymentMethod(PaymentMethod.CASH)
                .paymentStatus(PaymentStatus.PAID)
                .orderDate(LocalDateTime.now())
                .build();
    }

    private OrderResponseDto createOrderDto(String orderNumber, BigDecimal total) {
        OrderResponseDto dto = new OrderResponseDto();
        dto.setOrderNumber(orderNumber);
        dto.setTotalAmount(total);
        dto.setCustomerName("Test Client");
        return dto;
    }
}