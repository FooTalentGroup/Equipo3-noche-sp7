package com.stockia.stockia.dtos.order;

import com.stockia.stockia.enums.OrderStatus;
import com.stockia.stockia.enums.PaymentMethod;
import com.stockia.stockia.enums.PaymentStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

/**
 * DTO para los parámetros de búsqueda y filtrado de órdenes de venta.
 *
 * @param orderNumber   Número de orden (búsqueda parcial, case-insensitive)
 * @param customerName  Nombre del cliente (búsqueda parcial, case-insensitive)
 * @param status        Estado de la orden
 * @param paymentMethod Método de pago utilizado
 * @param paymentStatus Estado del pago
 * @param startDate     Fecha de inicio del rango de búsqueda (inclusive)
 * @param endDate       Fecha de fin del rango de búsqueda (inclusive)
 *
 * @author StockIA Team
 * @version 1.0
 * @since 2025-11-27
 */
@Schema(description = "Parámetros de búsqueda y filtrado para órdenes de venta")
public record OrderSearchRequestDto(

                @Schema(description = "Número de orden (búsqueda parcial, case-insensitive)", example = "ORD-20251127", requiredMode = Schema.RequiredMode.NOT_REQUIRED) String orderNumber,

                @Schema(description = "Nombre del cliente (búsqueda parcial, case-insensitive)", example = "María García", requiredMode = Schema.RequiredMode.NOT_REQUIRED) String customerName,

                @Schema(description = "Estado de la orden (PENDING, CONFIRMED, DELIVERED, CANCELLED)", example = "CONFIRMED", requiredMode = Schema.RequiredMode.NOT_REQUIRED) OrderStatus status,

                @Schema(description = "Método de pago (CASH, CARD, TRANSFER)", example = "CARD", requiredMode = Schema.RequiredMode.NOT_REQUIRED) PaymentMethod paymentMethod,

                @Schema(description = "Estado del pago (PENDING, PAID, REFUNDED)", example = "PAID", requiredMode = Schema.RequiredMode.NOT_REQUIRED) PaymentStatus paymentStatus,

                @Schema(description = "Fecha de inicio del rango de búsqueda (formato: YYYY-MM-DD)", example = "2025-11-01", requiredMode = Schema.RequiredMode.NOT_REQUIRED) LocalDate startDate,

                @Schema(description = "Fecha de fin del rango de búsqueda (formato: YYYY-MM-DD)", example = "2025-11-30", requiredMode = Schema.RequiredMode.NOT_REQUIRED) LocalDate endDate

) {
}
