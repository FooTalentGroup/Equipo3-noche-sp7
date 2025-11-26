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
    summary = "Actualizar producto existente (actualización parcial)",
    description = "Actualiza los datos de un producto. " +
                  "<br/><strong>Todos los campos son opcionales</strong> - solo envía los campos que deseas actualizar. " +
                  "<br/>Los campos no enviados mantendrán su valor actual. " +
                  "<br/><br/>Ejemplo de actualización solo del precio: <code>{\"price\": 999.99}</code>" +
                  "<br/>Ejemplo de actualización de nombre y stock: <code>{\"name\": \"nuevo nombre\", \"currentStock\": 25}</code>" +
                  "<br/><br/><strong>Solo accesible para usuarios con rol ADMIN.</strong>",
    security = @SecurityRequirement(name = "bearer-key"),
    requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
        description = "Datos del producto a actualizar (todos los campos son opcionales)",
        required = true,
        content = @Content(
            examples = {
                @ExampleObject(
                    name = "Actualizar solo precio",
                    summary = "Actualización parcial - solo precio",
                    value = "{\"price\": 999.99}"
                ),
                @ExampleObject(
                    name = "Actualizar nombre y stock",
                    summary = "Actualización parcial - nombre y stock",
                    value = "{\"name\": \"laptop hp pavilion\", \"currentStock\": 25}"
                ),
                @ExampleObject(
                    name = "Actualizar todos los campos",
                    summary = "Actualización completa",
                    value = "{\"name\": \"laptop hp pavilion 15\", \"categoryId\": \"123e4567-e89b-12d3-a456-426614174001\", \"price\": 1099.99, \"photoUrl\": \"https://example.com/images/laptop.jpg\", \"currentStock\": 20, \"minStock\": 8, \"isAvailable\": true}"
                )
            }
        )
    )
)
@ApiResponses({
    @ApiResponse(
        responseCode = "200",
        description = "Producto actualizado exitosamente",
        content = @Content(
            examples = @ExampleObject(
                value = "{\"success\":true,\"message\":\"Producto actualizado exitosamente\",\"data\":{\"id\":\"123e4567-e89b-12d3-a456-426614174000\",\"name\":\"laptop hp pavilion\",\"category\":{\"id\":\"123e4567-e89b-12d3-a456-426614174001\",\"name\":\"Electrónica\"},\"price\":1099.99,\"currentStock\":15,\"minStock\":5,\"isAvailable\":true,\"hasLowStock\":false}}"
            )
        )
    ),
    @ApiResponse(
        responseCode = "400",
        description = "Datos inválidos",
        content = @Content(
            examples = @ExampleObject(
                value = "{\"statusCode\":400,\"errorCode\":\"VALIDATION_ERROR\",\"message\":\"Falló la validación de los campos\",\"details\":[\"price: El precio debe ser mayor o igual a 0\"],\"timestamp\":\"2025-11-26T15:10:38.908929300Z\",\"path\":\"/api/products/123e4567-e89b-12d3-a456-426614174000\"}"
            )
        )
    ),
    @ApiResponse(
        responseCode = "404",
        description = "Producto o categoría no encontrada",
        content = @Content(
            examples = @ExampleObject(
                value = "{\"statusCode\":404,\"errorCode\":\"NOT_FOUND\",\"message\":\"No se encontró el producto con ID: 123e4567-e89b-12d3-a456-426614174000\",\"details\":[\"El producto especificado no existe en el sistema\"],\"timestamp\":\"2025-11-26T15:10:38.908929300Z\",\"path\":\"/api/products/123e4567-e89b-12d3-a456-426614174000\"}"
            )
        )
    ),
    @ApiResponse(
        responseCode = "409",
        description = "Producto duplicado",
        content = @Content(
            examples = @ExampleObject(
                value = "{\"statusCode\":409,\"errorCode\":\"CONFLICT\",\"message\":\"Producto duplicado. Ya existe un producto con el nombre: laptop hp\",\"details\":[\"El nombre del producto debe ser único\"],\"timestamp\":\"2025-11-26T15:10:38.908929300Z\",\"path\":\"/api/products/123e4567-e89b-12d3-a456-426614174000\"}"
            )
        )
    )
})
@SecurityResponses.RequiresAdmin
public @interface UpdateProductDoc {
}
