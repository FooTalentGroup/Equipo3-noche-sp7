package com.stockia.stockia.documentation.product;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Documentación del endpoint: POST /api/products
 * Registra un nuevo producto en el sistema.
 *
 * @author StockIA Team
 * @version 1.1
 * @since 2025-11-22
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Operation(
        summary = "Registrar nuevo producto",
        description = "Crea un nuevo producto. El nombre se guarda en lowercase automáticamente y debe ser único. " +
                "El stock inicial es 0 y minStock es 5 por defecto si no se especifica. " +
                "<strong>Solo accesible para usuarios con rol ADMIN o MANAGER.</strong>",
        security = @SecurityRequirement(name = "bearer-key")
)
@ApiResponses({
        @ApiResponse(
                responseCode = "201",
                description = "Producto creado exitosamente. Retorna el producto con los campos generados automáticamente.",
                content = @Content(
                        examples = @ExampleObject(
                                value = "{\"success\":true,\"message\":\"Producto registrado exitosamente\",\"data\":{\"id\":1,\"name\":\"laptop hp\",\"category\":{\"id\":1,\"name\":\"Electrónica\"},\"price\":999.99,\"photoUrl\":null,\"currentStock\":0,\"minStock\":5,\"isAvailable\":true,\"createdAt\":\"2025-11-21T10:30:00\",\"updatedAt\":\"2025-11-21T10:30:00\",\"hasLowStock\":true}}"
                        )
                )
        ),
        @ApiResponse(
                responseCode = "400",
                description = "Datos inválidos o formato incorrecto",
                content = @Content(
                        examples = @ExampleObject(
                                value = "{\"success\":false,\"message\":\"Errores de validación\",\"data\":{\"fields\":{\"name\":\"El nombre del producto es obligatorio\",\"price\":\"El precio debe ser mayor o igual a 0\"},\"errors\":[\"El nombre del producto es obligatorio\",\"El precio debe ser mayor o igual a 0\"]}}"
                        )
                )
        ),
        @ApiResponse(
                responseCode = "404",
                description = "Categoría no encontrada",
                content = @Content(
                        examples = @ExampleObject(
                                value = "{\"success\":false,\"message\":\"No se encontró la categoría con ID: 999\",\"data\":null}"
                        )
                )
        ),
        @ApiResponse(
                responseCode = "409",
                description = "Producto duplicado (nombre ya existe)",
                content = @Content(
                        examples = @ExampleObject(
                                value = "{\"success\":false,\"message\":\"Producto duplicado. Ya existe un producto con el nombre: laptop hp\",\"data\":null}"
                        )
                )
        ),
        @ApiResponse(
                responseCode = "401",
                description = "No autorizado - Token ausente o inválido",
                content = @Content(
                        examples = @ExampleObject(
                                value = "{\"success\":false,\"message\":\"Acceso no autorizado. Token inválido o ausente\",\"data\":null}"
                        )
                )
        ),
        @ApiResponse(
                responseCode = "403",
                description = "Acceso denegado - Se requiere rol ADMIN o MANAGER",
                content = @Content(
                        examples = @ExampleObject(
                                value = "{\"success\":false,\"message\":\"Acceso denegado. No tienes permisos para realizar esta acción\",\"data\":null}"
                        )
                )
        )
})
public @interface CreateProductDoc {
}

