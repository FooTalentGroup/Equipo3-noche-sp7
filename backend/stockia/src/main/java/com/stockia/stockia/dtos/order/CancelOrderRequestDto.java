package com.stockia.stockia.dtos.order;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para solicitar la cancelación de una orden.
 * Requiere un motivo obligatorio para registrar la razón de la cancelación.
 *
 * @author StockIA Team
 * @version 1.0
 * @since 2025-11-24
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Datos necesarios para cancelar una orden")
public class CancelOrderRequestDto {

    @NotBlank(message = "El motivo de cancelación es obligatorio")
    @Size(max = 500, message = "El motivo de cancelación no puede exceder 500 caracteres")
    @Schema(description = "Motivo de la cancelación de la orden", example = "Cliente solicitó devolución por producto defectuoso", required = true, maxLength = 500)
    private String cancelReason;
}
