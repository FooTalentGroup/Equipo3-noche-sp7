package com.stockia.stockia.documentation.client;

import com.stockia.stockia.documentation.common.SecurityResponses;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Documentación para el endpoint GET /api/clients
 * Obtiene clientes con paginación y filtros múltiples.
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Operation(
        summary = "Obtener clientes con paginación y filtros",
        description = """
                Obtiene una lista paginada de clientes. Permite filtrar por:
                - **name**: Nombre del cliente (búsqueda parcial, case-insensitive)
                - **email**: Email del cliente (búsqueda exacta)
                - **phone**: Teléfono del cliente (búsqueda exacta)
                - **isFrequent**: Filtrar por clientes frecuentes (true/false/null)
                
                Todos los filtros son opcionales. Si no se envía ningún filtro, devuelve todos los clientes.
                
                **Paginación:**
                - page: Número de página (inicia en 0)
                - size: Tamaño de página (por defecto 20)
                - sort: Campo para ordenar (ej: name,asc o name,desc)
                
                **Ejemplos de uso:**
                - GET /api/clients?page=0&size=20
                - GET /api/clients?name=Juan&page=0&size=10
                - GET /api/clients?email=juan@example.com
                - GET /api/clients?phone=+1234567890
                - GET /api/clients?isFrequent=true&page=0&size=20
                - GET /api/clients?name=Juan&isFrequent=true&sort=name,asc
                """,
        security = @SecurityRequirement(name = "bearer-key")
)
@ApiResponses({
        @ApiResponse(
                responseCode = "200",
                description = "Clientes obtenidos exitosamente",
                content = @Content(
                        mediaType = "application/json",
                        examples = @ExampleObject(
                                value = """
                                        {
                                          "success": true,
                                          "message": "Clientes obtenidos exitosamente",
                                          "data": {
                                            "content": [
                                              {
                                                "id": "123e4567-e89b-12d3-a456-426614174000",
                                                "name": "Juan Pérez",
                                                "email": "juan@example.com",
                                                "phone": "+1234567890",
                                                "isFrequent": true
                                              },
                                              {
                                                "id": "123e4567-e89b-12d3-a456-426614174001",
                                                "name": "María García",
                                                "email": "maria@example.com",
                                                "phone": "+1234567891",
                                                "isFrequent": false
                                              }
                                            ],
                                            "pageable": {
                                              "pageNumber": 0,
                                              "pageSize": 20,
                                              "sort": {
                                                "sorted": true,
                                                "unsorted": false,
                                                "empty": false
                                              }
                                            },
                                            "totalElements": 2,
                                            "totalPages": 1,
                                            "last": true,
                                            "first": true,
                                            "size": 20,
                                            "number": 0,
                                            "numberOfElements": 2,
                                            "empty": false
                                          }
                                        }
                                        """
                        )
                )
        ),
        @ApiResponse(
                responseCode = "400",
                description = "Parámetros de solicitud inválidos",
                content = @Content(
                        mediaType = "application/json",
                        examples = @ExampleObject(
                                value = """
                                        {
                                          "statusCode": 400,
                                          "errorCode": "BAD_REQUEST",
                                          "message": "Parámetros de solicitud inválidos",
                                          "details": ["page: El número de página no puede ser negativo"],
                                          "timestamp": "2025-11-26T15:10:38.908929300Z",
                                          "path": "/api/clients"
                                        }
                                        """
                        )
                )
        )
})
@SecurityResponses.Unauthorized
public @interface GetAllClientsWithFiltersDoc {
}

