package com.stockia.stockia.mappers;

import com.stockia.stockia.dtos.order.OrderItemResponseDto;
import com.stockia.stockia.dtos.order.OrderResponseDto;
import com.stockia.stockia.models.Order;
import com.stockia.stockia.models.OrderItem;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper para convertir entre entidades Order/OrderItem y sus DTOs.
 * Utiliza ProductMapper para mapear los productos dentro de los items.
 *
 * @author StockIA Team
 * @version 1.0
 * @since 2025-11-24
 */
@Component
@RequiredArgsConstructor
public class OrderMapper {

    private final ProductMapper productMapper;

    /**
     * Convierte una entidad OrderItem a DTO de respuesta.
     *
     * @param orderItem Entidad OrderItem
     * @return DTO de respuesta del item
     */
    public OrderItemResponseDto toItemResponseDto(OrderItem orderItem) {
        if (orderItem == null) {
            return null;
        }

        return OrderItemResponseDto.builder()
                .id(orderItem.getId())
                .product(productMapper.toResponseDto(orderItem.getProduct()))
                .quantity(orderItem.getQuantity())
                .unitPrice(orderItem.getUnitPrice())
                .itemTotal(orderItem.getItemTotal())
                .build();
    }

    /**
     * Convierte una lista de OrderItems a lista de DTOs de respuesta.
     *
     * @param items Lista de entidades OrderItem
     * @return Lista de DTOs de respuesta
     */
    public List<OrderItemResponseDto> toItemResponseDtoList(List<OrderItem> items) {
        if (items == null) {
            return List.of();
        }

        return items.stream()
                .map(this::toItemResponseDto)
                .collect(Collectors.toList());
    }

    /**
     * Convierte una entidad Order a DTO de respuesta.
     * Incluye todos los campos y relaciones (cliente, usuario, items, etc.).
     *
     * @param order Entidad Order
     * @return DTO de respuesta de la orden
     */
    public OrderResponseDto toResponseDto(Order order) {
        if (order == null) {
            return null;
        }

        return OrderResponseDto.builder()
                .id(order.getId())
                .orderNumber(order.getOrderNumber())
                .customerId(order.getCustomer() != null ? order.getCustomer().getId() : null)
                .customerName(order.getCustomer() != null ? order.getCustomer().getName() : null)
                .userId(order.getUser() != null ? order.getUser().getId() : null)
                .userName(order.getUser() != null ? order.getUser().getName() : null)
                .status(order.getStatus())
                .subtotal(order.getSubtotal())
                .discountAmount(order.getDiscountAmount())
                .totalAmount(order.getTotalAmount())
                .paymentMethod(order.getPaymentMethod())
                .paymentStatus(order.getPaymentStatus())
                .paymentNote(order.getPaymentNote())
                .orderDate(order.getOrderDate())
                .deliveredDate(order.getDeliveredDate())
                .cancelledDate(order.getCancelledDate())
                .cancelReason(order.getCancelReason())
                .cancelledById(order.getCancelledBy() != null ? order.getCancelledBy().getId() : null)
                .cancelledByName(order.getCancelledBy() != null ? order.getCancelledBy().getName() : null)
                .items(toItemResponseDtoList(order.getItems()))
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .build();
    }

    /**
     * Convierte una lista de entidades Order a lista de DTOs de respuesta.
     *
     * @param orders Lista de entidades Order
     * @return Lista de DTOs de respuesta
     */
    public List<OrderResponseDto> toResponseDtoList(List<Order> orders) {
        if (orders == null) {
            return List.of();
        }

        return orders.stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }
}
