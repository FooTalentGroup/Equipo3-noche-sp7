package com.stockia.stockia.dtos.client;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * DTO para los parámetros de búsqueda y filtrado de clientes.
 *
 * @param name Nombre del cliente (búsqueda parcial, case-insensitive)
 * @param email Email del cliente (búsqueda exacta)
 * @param phone Teléfono del cliente (búsqueda exacta)
 * @param isFrequent Filtrar por clientes frecuentes (true) o no frecuentes (false), null para todos
 *
 * @author StockIA Team
 * @version 1.0
 * @since 2025-11-26
 */
@Schema(description = "Parámetros de búsqueda y filtrado para clientes")
public record ClientSearchRequestDto(

    @Schema(description = "Nombre del cliente (búsqueda parcial, case-insensitive)",
            example = "Juan",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    String name,

    @Schema(description = "Email del cliente (búsqueda exacta)",
            example = "juan@example.com",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    String email,

    @Schema(description = "Teléfono del cliente (búsqueda exacta)",
            example = "+1234567890",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    String phone,

    @Schema(description = "Filtrar por clientes frecuentes (true=frecuentes, false=no frecuentes, null=todos)",
            example = "true",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    Boolean isFrequent

) {
}

