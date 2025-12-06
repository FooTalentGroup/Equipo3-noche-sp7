package com.stockia.stockia.documentation.inventoryMovement;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import java.lang.annotation.*;

/**
 * Documentación Swagger para POST /inventory/movements.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Operation(summary = "Registrar movimiento de inventario", description = """
          Registra un movimiento de inventario (entrada, salida o ajuste). \s
          Requiere autenticación y rol ADMIN. El usuario se obtiene automáticamente del token JWT.
        """, security = @SecurityRequirement(name = "bearer-key"))
@ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Movimiento registrado correctamente", content = @Content(mediaType = "application/json", schema = @Schema(example = """
                    {
                      "id": "550e8400-e29b-41d4-a716-446655440000",
                      "product": {
                        "id": "123e4567-e89b-12d3-a456-426614174000",
                        "name": "Producto ejemplo"
                      },
                      "user": {
                        "id": "789e0123-e89b-12d3-a456-426614174000",
                        "name": "Admin Usuario"
                      },
                      "movementType": "IN",
                      "quantity": 50,
                      "reason": "Compra a proveedor",
                      "newStock": 150,
                      "purchaseCost": 1250.00,
                      "createdAt": "2025-11-24T00:30:00"
                    }
                """))),
        @ApiResponse(responseCode = "400", description = "Solicitud inválida: stock insuficiente o error de validación", content = @Content(mediaType = "application/json", examples = {
                @ExampleObject(name = "Stock insuficiente", summary = "Cuando no hay suficiente stock para salida", value = """
                            {
                              "statusCode": 400,
                              "message": "Stock insuficiente. Stock actual: 10, cantidad requerida: 50",
                              "errorCode": "INSUFFICIENT_STOCK",
                              "details": [],
                              "path": "/inventory/movements"
                            }
                        """),
                @ExampleObject(name = "Error de validación", summary = "Cuando faltan campos obligatorios", value = """
                            {
                              "statusCode": 400,
                              "message": "Falló la validación de los campos",
                              "errorCode": "VALIDATION_ERROR",
                              "details": [
                                "productId: El producto es requerido",
                                "quantity: La cantidad es requerida"
                              ],
                              "path": "/inventory/movements"
                            }
                        """)
        })),
        @ApiResponse(responseCode = "401", description = "No autorizado o token inválido", content = @Content(mediaType = "application/json", schema = @Schema(example = """
                    {
                      "statusCode": 401,
                      "errorCode": "AUTH_ERROR",
                      "message": "Acceso no autorizado. Token inválido o ausente.",
                      "details": [],
                      "path": "/inventory/movements"
                    }
                """))),
        @ApiResponse(responseCode = "403", description = "Acceso denegado - Se requiere rol ADMIN", content = @Content(mediaType = "application/json", schema = @Schema(example = """
                    {
                      "statusCode": 403,
                      "errorCode": "ACCESS_DENIED",
                      "message": "No tienes permisos para realizar esta acción",
                      "details": [],
                      "path": "/inventory/movements"
                    }
                """))),
        @ApiResponse(responseCode = "404", description = "Producto no encontrado", content = @Content(mediaType = "application/json", schema = @Schema(example = """
                    {
                      "statusCode": 404,
                      "message": "Producto no encontrado con ID: 123e4567-e89b-12d3-a456-426614174000",
                      "errorCode": "PRODUCT_NOT_FOUND",
                      "details": [],
                      "path": "/inventory/movements"
                    }
                """))),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(mediaType = "application/json", schema = @Schema(example = """
                    {
                      "statusCode": 500,
                      "message": "Ocurrió un error interno en el servidor",
                      "errorCode": "INTERNAL_ERROR",
                      "details": ["java.lang.NullPointerException: ..."],
                      "path": "/inventory/movements"
                    }
                """)))
})
public @interface RegisterMovementEndpointDoc {
}
