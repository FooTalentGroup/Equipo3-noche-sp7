package com.stockia.stockia.dtos.order;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Datos para editar los items de una orden existente")
public class EditOrderRequestDto {

    @NotEmpty(message = "La orden debe contener al menos un producto")
    @Valid
    @Schema(description = "Lista actualizada de productos con sus cantidades", required = true)
    private List<OrderItemRequestDto> items;
}
