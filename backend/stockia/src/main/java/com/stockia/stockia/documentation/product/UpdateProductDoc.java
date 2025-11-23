package com.stockia.stockia.documentation.product;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import com.stockia.stockia.documentation.common.SecurityResponses;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Documentación del endpoint: PUT /api/products/{id}
 * Actualiza los datos de un producto existente.
 *
 * @author StockIA Team
 * @version 1.1
 * @since 2025-11-22
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Operation(
    summary = "Actualizar producto existente",
    description = "Actualiza los datos de un producto. Solo se modifican los campos proporcionados. " +
                  "<strong>Solo accesible para usuarios con rol ADMIN.</strong>",
    security = @SecurityRequirement(name = "bearer-key")
)
@ApiResponses({
    @ApiResponse(
        responseCode = "200",
        description = "Producto actualizado exitosamente",
        content = @Content(
            examples = @ExampleObject(
                value = "{\"success\":true,\"message\":\"Producto actualizado exitosamente\",\"data\":{\"id\":1,\"name\":\"laptop hp pavilion\",\"category\":{\"id\":1,\"name\":\"Electrónica\"},\"price\":1099.99,\"currentStock\":15,\"minStock\":5,\"isAvailable\":true,\"hasLowStock\":false}}"
            )
        )
    ),
    @ApiResponse(
        responseCode = "400",
        description = "Datos inválidos",
        content = @Content(
            examples = @ExampleObject(
                value = "{\"success\":false,\"message\":\"Errores de validación\",\"data\":{\"fields\":{\"price\":\"El precio debe ser mayor o igual a 0\"},\"errors\":[\"El precio debe ser mayor o igual a 0\"]}}"
            )
        )
    ),
    @ApiResponse(
        responseCode = "404",
        description = "Producto o categoría no encontrada",
        content = @Content(
            examples = @ExampleObject(
                value = "{\"success\":false,\"message\":\"No se encontró el producto con ID: 999\",\"data\":null}"
            )
        )
    ),
    @ApiResponse(
        responseCode = "409",
        description = "Producto duplicado",
        content = @Content(
            examples = @ExampleObject(
                value = "{\"success\":false,\"message\":\"Producto duplicado. Ya existe un producto con el nombre: laptop hp\",\"data\":null}"
            )
        )
    )
})
@SecurityResponses.RequiresAdmin
public @interface UpdateProductDoc {
}

