package com.stockia.stockia.dtos.order;

import com.stockia.stockia.enums.OrderStatus;
import com.stockia.stockia.enums.PaymentMethod;
import com.stockia.stockia.enums.PaymentStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * DTO de respuesta para órdenes de venta.
 * Incluye información completa de la orden con todos sus items,
 * cliente, usuario responsable y detalles de pago.
 *
 * @author StockIA Team
 * @version 1.0
 * @since 2025-11-24
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Información completa de una orden de venta")
public class OrderResponseDto {

    @Schema(description = "ID único de la orden", example = "880e8400-e29b-41d4-a716-446655440000")
    private UUID id;

    @Schema(description = "Número de orden generado", example = "ORD-20251124-0001")
    private String orderNumber;

    @Schema(description = "ID del cliente", example = "660e8400-e29b-41d4-a716-446655440000")
    private UUID customerId;

    @Schema(description = "Nombre del cliente", example = "Juan Pérez")
    private String customerName;

    @Schema(description = "ID del usuario que creó la orden", example = "990e8400-e29b-41d4-a716-446655440000")
    private UUID userId;

    @Schema(description = "Nombre del usuario que creó la orden", example = "María García")
    private String userName;

    @Schema(description = "Estado actual de la orden", example = "CONFIRMED")
    private OrderStatus status;

    @Schema(description = "Subtotal de la orden (antes de descuentos)", example = "500.00")
    private BigDecimal subtotal;

    @Schema(description = "Monto de descuento aplicado", example = "50.00")
    private BigDecimal discountAmount;

    @Schema(description = "Total final de la orden", example = "450.00")
    private BigDecimal totalAmount;

    @Schema(description = "Método de pago utilizado", example = "CASH")
    private PaymentMethod paymentMethod;

    @Schema(description = "Estado del pago", example = "PAID")
    private PaymentStatus paymentStatus;

    @Schema(description = "Nota adicional sobre el pago", example = "Pago en efectivo")
    private String paymentNote;

    @Schema(description = "Fecha y hora de creación de la orden", example = "2025-11-24T15:30:00")
    private LocalDateTime orderDate;

    @Schema(description = "Fecha y hora de entrega (si aplica)", example = "2025-11-24T16:00:00")
    private LocalDateTime deliveredDate;

    @Schema(description = "Fecha y hora de cancelación (si aplica)", example = "2025-11-24T17:00:00")
    private LocalDateTime cancelledDate;

    @Schema(description = "Motivo de cancelación (si aplica)", example = "Cliente solicitó cancelación")
    private String cancelReason;

    @Schema(description = "ID del usuario que canceló la orden (si aplica)", example = "990e8400-e29b-41d4-a716-446655440000")
    private UUID cancelledById;

    @Schema(description = "Nombre del usuario que canceló la orden (si aplica)", example = "María García")
    private String cancelledByName;

    @Schema(description = "Lista de items/productos en la orden")
    private List<OrderItemResponseDto> items;

    @Schema(description = "Fecha y hora de creación del registro", example = "2025-11-24T15:30:00")
    private LocalDateTime createdAt;

    @Schema(description = "Fecha y hora de última actualización del registro", example = "2025-11-24T16:00:00")
    private LocalDateTime updatedAt;
}
