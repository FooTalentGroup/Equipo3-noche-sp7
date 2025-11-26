package com.stockia.stockia.documentation.category;

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
 * Documentación del endpoint GET /api/categories - Buscar y Listar con Filtros.
 *
 * @author StockIA Team
 * @version 1.1
 * @since 2025-11-25
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Operation(
    summary = "Buscar y listar categorías con filtros dinámicos",
    description = """
        Obtiene una lista paginada de categorías con filtros opcionales:
        
        **Parámetros de filtrado disponibles:**
        - `name` (String): Búsqueda parcial por nombre de categoría (case-insensitive)
        - `isActive` (Boolean): Filtrar por estado activo
          - `true`: Solo categorías activas
          - `false`: Solo categorías inactivas
          - `null` o ausente: Todas las categorías
        - `deleted` (Boolean): Filtrar por estado de eliminación (soft delete)
          - `true`: Solo categorías eliminadas (papelera)
          - `false`: Solo categorías no eliminadas
          - `null` o ausente: Todas las categorías
        
        **Paginación (parámetros estándar de Spring):**
        - `page` (int): Número de página (default: 0)
        - `size` (int): Tamaño de página (default: 20)
        - `sort` (String): Ordenamiento (ej: "name,asc" o "name,desc")
        
        **Ejemplos de uso:**
        - Todas las categorías: `/api/categories?page=0&size=10`
        - Categorías activas no eliminadas: `/api/categories?isActive=true&deleted=false`
        - Buscar por nombre: `/api/categories?name=Electr`
        - Categorías eliminadas (papelera): `/api/categories?deleted=true`
        - Ordenar alfabéticamente: `/api/categories?sort=name,asc`
        
        <strong>Solo accesible para usuarios con rol ADMIN o MANAGER.</strong>
        """,
    security = @SecurityRequirement(name = "bearer-key")
)
@ApiResponses(value = {
    @ApiResponse(
        responseCode = "200",
        description = "Página de categorías obtenida exitosamente",
        content = @Content(
            mediaType = "application/json",
            examples = @ExampleObject(
                value = """
                {
                  "success": true,
                  "message": "Categorías obtenidas exitosamente",
                  "data": {
                    "content": [
                      {
                        "id": "550e8400-e29b-41d4-a716-446655440000",
                        "name": "Electrónica",
                        "description": "Dispositivos electrónicos y tecnología",
                        "isActive": true,
                        "productCount": 5
                      },
                      {
                        "id": "550e8400-e29b-41d4-a716-446655440001",
                        "name": "Oficina",
                        "description": "Suministros de oficina",
                        "isActive": true,
                        "productCount": 3
                      }
                    ],
                    "pageable": {
                      "pageNumber": 0,
                      "pageSize": 10,
                      "sort": {
                        "sorted": true,
                        "unsorted": false,
                        "empty": false
                      },
                      "offset": 0,
                      "paged": true,
                      "unpaged": false
                    },
                    "totalElements": 2,
                    "totalPages": 1,
                    "last": true,
                    "first": true,
                    "size": 10,
                    "number": 0,
                    "numberOfElements": 2,
                    "empty": false,
                    "sort": {
                      "sorted": true,
                      "unsorted": false,
                      "empty": false
                    }
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
                  "path": "/api/categories"
                }
                """
            )
        )
    )
})
@SecurityResponses.RequiresAdminOrManager
public @interface GetAllCategoriesDoc {}

