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
 * Documentación del endpoint: GET /api/products
 * Lista todos los productos con filtros opcionales y paginación.
 *
 * @author StockIA Team
 * @version 3.0
 * @since 2025-11-25
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Operation(
        summary = "Listar todos los productos con filtros opcionales y paginación",
        description = "Retorna una página de productos del sistema con capacidad de filtrado avanzado. " +
                "<br><br><strong>Parámetros de filtrado:</strong>" +
                "<ul>" +
                "<li><strong>deleted</strong> (boolean, default=false): Incluir solo productos eliminados</li>" +
                "<li><strong>includeInactive</strong> (boolean, default=false): Incluir productos inactivos (isAvailable=false)</li>" +
                "<li><strong>lowStock</strong> (boolean, default=false): Filtrar solo productos con stock bajo</li>" +
                "<li><strong>q</strong> (string): Búsqueda por nombre del producto (case-insensitive)</li>" +
                "<li><strong>categoryId</strong> (UUID): Filtrar por categoría específica</li>" +
                "</ul>" +
                "<br><strong>Parámetros de paginación:</strong>" +
                "<ul>" +
                "<li><strong>page</strong> (int, default=0): Número de página (0-indexed)</li>" +
                "<li><strong>size</strong> (int, default=20): Cantidad de elementos por página</li>" +
                "<li><strong>sort</strong> (string): Campo(s) de ordenamiento. Ejemplos: 'name,asc', 'price,desc', 'currentStock'</li>" +
                "</ul>" +
                "<br><strong>Ejemplos de uso:</strong>" +
                "<ul>" +
                "<li><code>GET /api/products</code> - Primera página de productos activos</li>" +
                "<li><code>GET /api/products?page=0&size=10</code> - 10 productos por página</li>" +
                "<li><code>GET /api/products?deleted=true</code> - Productos eliminados</li>" +
                "<li><code>GET /api/products?includeInactive=true</code> - Incluye productos inactivos</li>" +
                "<li><code>GET /api/products?lowStock=true</code> - Solo productos con stock bajo</li>" +
                "<li><code>GET /api/products?q=laptop&sort=name,asc</code> - Busca 'laptop' ordenado por nombre</li>" +
                "<li><code>GET /api/products?categoryId=123e4567-e89b-12d3-a456-426614174000&page=1</code> - Productos de una categoría, página 2</li>" +
                "<li><code>GET /api/products?q=laptop&lowStock=true&sort=currentStock,asc</code> - Combinación con ordenamiento</li>" +
                "</ul>" +
                "<br><strong>Solo accesible para usuarios con rol ADMIN o MANAGER.</strong>",
        security = @SecurityRequirement(name = "bearer-key")
)
@ApiResponses({
        @ApiResponse(
                responseCode = "200",
                description = "Página de productos obtenida exitosamente",
                content = @Content(
                        examples = @ExampleObject(
                                value = "{\"success\":true,\"message\":\"Productos obtenidos exitosamente\",\"data\":{\"content\":[{\"id\":\"123e4567-e89b-12d3-a456-426614174000\",\"name\":\"laptop hp\",\"category\":{\"id\":\"123e4567-e89b-12d3-a456-426614174001\",\"name\":\"Electrónica\"},\"price\":999.99,\"currentStock\":10,\"minStock\":5,\"isAvailable\":true,\"hasLowStock\":false}],\"pageable\":{\"pageNumber\":0,\"pageSize\":20},\"totalElements\":1,\"totalPages\":1,\"last\":true,\"first\":true,\"size\":20,\"number\":0,\"numberOfElements\":1,\"empty\":false}}"
                        )
                )
        ),
        @ApiResponse(
                responseCode = "400",
                description = "Parámetros de búsqueda inválidos",
                content = @Content(
                        examples = @ExampleObject(
                                value = "{\"statusCode\":400,\"errorCode\":\"BAD_REQUEST\",\"message\":\"Parámetros de búsqueda inválidos\",\"details\":[\"Los parámetros proporcionados no son válidos\"],\"timestamp\":\"2025-11-26T15:10:38.908929300Z\",\"path\":\"/api/products\"}"
                        )
                )
        )
})
@SecurityResponses.RequiresAdminOrManager
public @interface GetAllProductsDoc {
}

