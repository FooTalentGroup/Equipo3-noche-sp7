package com.stockia.stockia.dtos.report;

import com.stockia.stockia.enums.PaymentMethod;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO para representar la distribución de ventas por método de pago.
 * Usado para generar el gráfico de dona del reporte de ventas.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Distribución de ventas por método de pago para gráfico de dona")
public class PaymentMethodDistributionDto {

    @Schema(description = "Método de pago", example = "CASH")
    private PaymentMethod paymentMethod;

    @Schema(description = "Monto total acumulado", example = "500000.00")
    private BigDecimal totalAmount;

    @Schema(description = "Porcentaje del total", example = "20.0")
    private Double percentage;

    public PaymentMethodDistributionDto(PaymentMethod paymentMethod, BigDecimal totalAmount) {
        this.paymentMethod = paymentMethod;
        this.totalAmount = totalAmount != null ? totalAmount : BigDecimal.ZERO;
    }
}
