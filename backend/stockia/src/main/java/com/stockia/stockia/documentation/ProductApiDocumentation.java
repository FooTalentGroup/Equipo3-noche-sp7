package com.stockia.stockia.documentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Documentación Swagger para los endpoints de productos.
 * Contiene todas las anotaciones de OpenAPI para documentar la API REST.
 *
 * @author StockIA Team
 * @version 1.0
 * @since 2025-11-20
 */
public class ProductApiDocumentation {

    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    @Tag(name = "Productos", description = "API para gestión de productos del inventario")
    public @interface ProductControllerTag {}

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(
        summary = "Registrar nuevo producto",
        description = "Crea un nuevo producto. El nombre se guarda en lowercase automáticamente y debe ser único. " +
                      "El stock inicial es 0 y minStock es 5 por defecto si no se especifica."
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "201",
            description = "Producto creado exitosamente. Retorna el producto con los campos generados automáticamente.",
            content = @io.swagger.v3.oas.annotations.media.Content(
                examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                    value = "{\"success\":true,\"message\":\"Producto registrado exitosamente\",\"data\":{\"id\":1,\"name\":\"laptop hp\",\"category\":{\"id\":1,\"name\":\"Electrónica\"},\"price\":999.99,\"photoUrl\":null,\"currentStock\":0,\"minStock\":5,\"isAvailable\":true,\"createdAt\":\"2025-11-21T10:30:00\",\"updatedAt\":\"2025-11-21T10:30:00\",\"hasLowStock\":true}}"
                )
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Datos inválidos o formato incorrecto",
            content = @io.swagger.v3.oas.annotations.media.Content(
                examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                    value = "{\"success\":false,\"message\":\"Errores de validación\",\"data\":{\"fields\":{\"name\":\"El nombre del producto es obligatorio\",\"price\":\"El precio debe ser mayor o igual a 0\"},\"errors\":[\"El nombre del producto es obligatorio\",\"El precio debe ser mayor o igual a 0\"]}}"
                )
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Categoría no encontrada",
            content = @io.swagger.v3.oas.annotations.media.Content(
                examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                    value = "{\"success\":false,\"message\":\"No se encontró la categoría con ID: 999\",\"data\":null}"
                )
            )
        ),
        @ApiResponse(
            responseCode = "409",
            description = "Producto duplicado (nombre ya existe)",
            content = @io.swagger.v3.oas.annotations.media.Content(
                examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                    value = "{\"success\":false,\"message\":\"Producto duplicado. Ya existe un producto con el nombre: laptop hp\",\"data\":null}"
                )
            )
        )
    })
    public @interface CreateProductDoc {}

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(
        summary = "Obtener producto por ID",
        description = "Retorna la información completa de un producto específico"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Producto encontrado"),
        @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    public @interface GetProductByIdDoc {}

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(
        summary = "Listar productos activos",
        description = "Retorna todos los productos activos y disponibles. " +
                      "Opcionalmente puede filtrar por búsqueda de texto (q) y/o categoría (categoryId)"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Lista de productos obtenida exitosamente",
            content = @io.swagger.v3.oas.annotations.media.Content(
                examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                    value = "{\"success\":true,\"message\":\"3 producto(s) encontrado(s)\",\"data\":[{\"id\":1,\"name\":\"laptop hp\",\"category\":{\"id\":1,\"name\":\"Electrónica\"},\"price\":999.99,\"currentStock\":10,\"minStock\":5,\"isAvailable\":true,\"hasLowStock\":false}]}"
                )
            )
        )
    })
    public @interface GetAllProductsDoc {}



    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(
        summary = "Actualizar producto existente",
        description = "Actualiza los datos de un producto. Solo se modifican los campos proporcionados"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Producto actualizado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos"),
        @ApiResponse(responseCode = "404", description = "Producto o categoría no encontrada"),
        @ApiResponse(responseCode = "409", description = "Producto duplicado")
    })
    public @interface UpdateProductDoc {}

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(
        summary = "Eliminar producto (soft delete)",
        description = "Marca el producto como eliminado sin borrarlo físicamente. Se guarda en el historial"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Producto eliminado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    public @interface DeleteProductDoc {}

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(
        summary = "Listar productos eliminados",
        description = "Retorna el historial de productos eliminados (soft delete)"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista de productos eliminados")
    })
    public @interface GetDeletedProductsDoc {}

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(
        summary = "Gestión avanzada de productos",
        description = "Endpoint para administradores que permite aplicar múltiples filtros. " +
                      "Filtros disponibles: " +
                      "- includeInactive: incluye productos con isAvailable=false (por defecto false) " +
                      "- lowStock: solo productos con stock bajo (por defecto false) " +
                      "- q: búsqueda por texto en el nombre " +
                      "- categoryId: filtrar por categoría específica"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Lista de productos obtenida exitosamente",
            content = @io.swagger.v3.oas.annotations.media.Content(
                examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                    value = "{\"success\":true,\"message\":\"2 producto(s) encontrado(s)\",\"data\":[{\"id\":1,\"name\":\"laptop hp\",\"category\":{\"id\":1,\"name\":\"Electrónica\"},\"price\":999.99,\"currentStock\":2,\"minStock\":5,\"isAvailable\":true,\"hasLowStock\":true}]}"
                )
            )
        )
    })
    public @interface GetProductsManagementDoc {}

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(
        summary = "Restaurar producto eliminado",
        description = "Recupera un producto previamente eliminado (soft delete), restaurando su disponibilidad"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Producto restaurado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Producto no encontrado"),
        @ApiResponse(responseCode = "400", description = "El producto no está eliminado")
    })
    public @interface RestoreProductDoc {}

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(
        summary = "Eliminar producto permanentemente",
        description = "Elimina físicamente el producto de la base de datos. Esta acción es irreversible"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Producto eliminado permanentemente"),
        @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    public @interface PermanentDeleteProductDoc {}


    @Target(ElementType.PARAMETER)
    @Retention(RetentionPolicy.RUNTIME)
    @Parameter(description = "ID del producto", example = "1")
    public @interface ProductIdParam {}

    @Target(ElementType.PARAMETER)
    @Retention(RetentionPolicy.RUNTIME)
    @Parameter(description = "Parte del nombre del producto a buscar", example = "laptop")
    public @interface ProductNameParam {}

    @Target(ElementType.PARAMETER)
    @Retention(RetentionPolicy.RUNTIME)
    @Parameter(description = "ID de la categoría", example = "1")
    public @interface CategoryIdParam {}

    @Target(ElementType.PARAMETER)
    @Retention(RetentionPolicy.RUNTIME)
    @Parameter(
        description = "Incluir productos inactivos (isAvailable=false). Por defecto false (solo activos)",
        example = "false"
    )
    public @interface IncludeInactiveParam {}

    @Target(ElementType.PARAMETER)
    @Retention(RetentionPolicy.RUNTIME)
    @Parameter(
        description = "Filtrar solo productos con stock bajo (currentStock <= minStock). Por defecto false (todos)",
        example = "false"
    )
    public @interface LowStockParam {}
}

