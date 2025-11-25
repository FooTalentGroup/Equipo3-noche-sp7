package com.stockia.stockia.dtos.order;

import com.stockia.stockia.enums.PaymentMethod;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

/**
 * DTO para solicitar la creación de una nueva orden de venta.
 * Contiene toda la información necesaria para registrar una venta completa.
 * El usuario responsable se obtiene automáticamente del contexto de seguridad.
 *
 * @author StockIA Team
 * @version 1.0
 * @since 2025-11-24
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Datos necesarios para crear una nueva orden de venta")
public class OrderRequestDto {

    @NotNull(message = "El ID del cliente es obligatorio")
    @Schema(description = "ID del cliente que realiza la compra", example = "660e8400-e29b-41d4-a716-446655440000", required = true)
    private UUID customerId;

    @NotEmpty(message = "La orden debe contener al menos un producto")
    @Valid
    @Schema(description = "Lista de productos a vender con sus cantidades", required = true)
    private List<OrderItemRequestDto> items;

    @NotNull(message = "El método de pago es obligatorio")
    @Schema(description = "Método de pago seleccionado", example = "CASH", required = true, allowableValues = { "CASH",
            "CARD", "TRANSFER" })
    private PaymentMethod paymentMethod;

    @Size(max = 500, message = "La nota de pago no puede exceder 500 caracteres")
    @Schema(description = "Nota o comentario adicional sobre el pago (opcional)", example = "Pago en efectivo con cambio", maxLength = 500)
    private String paymentNote;

    @DecimalMin(value = "0.0", message = "El descuento debe ser mayor o igual a 0")
    @Schema(description = "Descuento aplicado al total de la orden (opcional)", example = "10.00", minimum = "0")
    private BigDecimal discountAmount;
}
