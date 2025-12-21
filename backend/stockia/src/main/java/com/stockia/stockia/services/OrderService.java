package com.stockia.stockia.services;

import com.stockia.stockia.dtos.order.CancelOrderRequestDto;
import com.stockia.stockia.dtos.order.EditOrderRequestDto;
import com.stockia.stockia.dtos.order.OrderRequestDto;
import com.stockia.stockia.dtos.order.OrderResponseDto;
import com.stockia.stockia.dtos.order.OrderSearchRequestDto;
import com.stockia.stockia.enums.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

/**
 * Servicio para la gestión de órdenes de venta.
 * Define las operaciones disponibles para el módulo de ventas.
 */
public interface OrderService {

    OrderResponseDto createOrder(OrderRequestDto dto);

    OrderResponseDto getOrderById(UUID id);

    OrderResponseDto getOrderByOrderNumber(String orderNumber);

    List<OrderResponseDto> getAllOrders();

    Page<OrderResponseDto> searchOrders(OrderSearchRequestDto searchParams, Pageable pageable);

    List<OrderResponseDto> getOrdersByStatus(OrderStatus status);

    OrderResponseDto confirmOrder(UUID id);

    OrderResponseDto editOrder(UUID id, EditOrderRequestDto dto);

    OrderResponseDto cancelOrder(UUID id, CancelOrderRequestDto dto);

    OrderResponseDto markAsDelivered(UUID id);

    byte[] generateOrderPdf(UUID id);
}
