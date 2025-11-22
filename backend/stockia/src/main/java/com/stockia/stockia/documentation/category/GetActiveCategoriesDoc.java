package com.stockia.stockia.documentation.category;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Documentación del endpoint GET /api/categories/active - Solo Activas.
 *
 * @author StockIA Team
 * @version 1.0
 * @since 2025-11-22
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Operation(
    summary = "Listar categorías activas",
    description = "Obtiene solo las categorías que están activas (isActive = true)"
)
@ApiResponses(value = {
    @ApiResponse(
        responseCode = "200",
        description = "Lista de categorías activas obtenida exitosamente"
    )
})
public @interface GetActiveCategoriesDoc {}

