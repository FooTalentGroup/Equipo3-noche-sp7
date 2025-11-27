package com.stockia.stockia.documentation.order;

import com.stockia.stockia.documentation.common.SecurityResponses;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Documentación del endpoint: GET /api/orders
 * Busca y lista órdenes con paginación y filtros múltiples.
 *
 * @author StockIA Team
 * @version 1.0
 * @since 2025-11-27
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Operation(summary = "Buscar y listar órdenes con paginación", description = """
                Busca órdenes con paginación y filtros múltiples opcionales:

                **Filtros disponibles:**
                - `orderNumber`: Búsqueda parcial por número de orden (ej: "ORD-20251127")
                - `customerName`: Búsqueda parcial por nombre del cliente (ej: "María")
                - `status`: Filtrar por estado (PENDING, CONFIRMED, DELIVERED, CANCELLED)
                - `paymentMethod`: Filtrar por método de pago (CASH, DEBIT_CARD, CREDIT_CARD, BANK_TRANSFER)
                - `paymentStatus`: Filtrar por estado del pago (PENDING, PAID, REFUNDED)
                - `startDate`: Fecha de inicio del rango (formato: YYYY-MM-DD)
                - `endDate`: Fecha fin del rango (formato: YYYY-MM-DD)

                **Paginación y ordenamiento:**
                - `page`: Número de página (default: 0)
                - `size`: Tamaño de página (default: 20)
                - `sort`: Campo y dirección de ordenamiento (ej: "orderDate,desc" o "totalAmount,asc")

                **Ejemplos:**
                - `/api/orders` - Todas las órdenes paginadas
                - `/api/orders?status=CONFIRMED&page=0&size=10` - Órdenes confirmadas
                - `/api/orders?customerName=María&sort=orderDate,desc` - Búsqueda por cliente
                - `/api/orders?startDate=2025-11-01&endDate=2025-11-30` - Rango de fechas

                <strong>Solo accesible para usuarios con rol ADMIN o MANAGER.</strong>
                """, security = @SecurityRequirement(name = "bearer-key"))
@ApiResponses({
                @ApiResponse(responseCode = "200", description = "Órdenes encontradas exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(example = """
                                {
                                  "success": true,
                                  "message": "25 orden(es) encontrada(s)",
                                  "data": {
                                    "content": [
                                      {
                                        "id": "770e8400-e29b-41d4-a716-446655440002",
                                        "orderNumber": "ORD-20251127-0001",
                                        "customer": {
                                          "id": "550e8400-e29b-41d4-a716-446655440000",
                                          "name": "María García López",
                                          "email": "maria.garcia@example.com"
                                        },
                                        "status": "CONFIRMED",
                                        "totalAmount": 25000.00,
                                        "paymentMethod": "CREDIT_CARD",
                                        "paymentStatus": "PAID",
                                        "orderDate": "2025-11-27T10:30:00"
                                      },
                                      {
                                        "id": "770e8400-e29b-41d4-a716-446655440003",
                                        "orderNumber": "ORD-20251127-0002",
                                        "customer": {
                                          "id": "550e8400-e29b-41d4-a716-446655440001",
                                          "name": "Juan Pérez",
                                          "email": "juan.perez@example.com"
                                        },
                                        "status": "DELIVERED",
                                        "totalAmount": 15000.00,
                                        "paymentMethod": "CASH",
                                        "paymentStatus": "PAID",
                                        "orderDate": "2025-11-27T09:15:00"
                                      }
                                    ],
                                    "pageable": {
                                      "pageNumber": 0,
                                      "pageSize": 20,
                                      "sort": {
                                        "sorted": true,
                                        "orders": [{"property": "orderDate", "direction": "DESC"}]
                                      }
                                    },
                                    "totalElements": 25,
                                    "totalPages": 2,
                                    "last": false,
                                    "first": true,
                                    "size": 20,
                                    "number": 0,
                                    "numberOfElements": 20,
                                    "empty": false
                                  }
                                }
                                """)))
})
@SecurityResponses.RequiresAdminOrManager
public @interface GetAllOrdersDoc {
}
