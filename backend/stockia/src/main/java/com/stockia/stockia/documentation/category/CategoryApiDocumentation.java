package com.stockia.stockia.documentation.category;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Documentación centralizada de la API de Categorías para Swagger/OpenAPI.
 *
 * @deprecated Este archivo ha sido segmentado en archivos individuales por endpoint.
 *             Usar los archivos individuales en su lugar:
 *             - CategoryControllerTag.java
 *             - CategoryIdParam.java
 *             - CreateCategoryDoc.java
 *             - GetAllCategoriesDoc.java
 *             - GetActiveCategoriesDoc.java
 *             - GetCategoryByIdDoc.java
 *             - UpdateCategoryDoc.java
 *             - DeactivateCategoryDoc.java
 *             - ActivateCategoryDoc.java
 *             - DeleteCategoryDoc.java
 *             - GetDeletedCategoriesDoc.java
 *             - RestoreCategoryDoc.java
 *             - PermanentDeleteCategoryDoc.java
 *
 * @author StockIA Team
 * @version 1.0
 * @since 2025-11-20
 */
@Deprecated(since = "1.1.1", forRemoval = true)
public class CategoryApiDocumentation {

    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    @Tag(
        name = "Categorías",
        description = "Gestión de categorías de productos. Permite crear, consultar, actualizar y desactivar categorías."
    )
    public @interface CategoryControllerTag {}

    // ============================================
    // POST /api/categories - Crear Categoría
    // ============================================
    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(
        summary = "Crear nueva categoría",
        description = "Crea una nueva categoría de productos. El nombre debe ser único."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Categoría creada exitosamente",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    value = """
                    {
                      "success": true,
                      "message": "Categoría creada exitosamente",
                      "data": {
                        "id": 8,
                        "name": "Deportes",
                        "description": "Artículos deportivos y equipamiento",
                        "isActive": true,
                        "productCount": 0
                      }
                    }
                    """
                )
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Datos de entrada inválidos",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    value = """
                    {
                      "success": false,
                      "message": "El nombre de la categoría es obligatorio"
                    }
                    """
                )
            )
        ),
        @ApiResponse(
            responseCode = "409",
            description = "Ya existe una categoría con ese nombre",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    value = """
                    {
                      "success": false,
                      "message": "Ya existe una categoría con el nombre: Deportes"
                    }
                    """
                )
            )
        )
    })
    public @interface CreateCategoryDoc {}

    // ============================================
    // GET /api/categories - Listar Todas
    // ============================================
    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(
        summary = "Listar todas las categorías",
        description = "Obtiene la lista completa de categorías (activas e inactivas)"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Lista de categorías obtenida exitosamente",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    value = """
                    {
                      "success": true,
                      "message": "Categorías obtenidas exitosamente",
                      "data": [
                        {
                          "id": 1,
                          "name": "Electrónica",
                          "description": "Dispositivos electrónicos y tecnología",
                          "isActive": true,
                          "productCount": 5
                        },
                        {
                          "id": 2,
                          "name": "Oficina",
                          "description": "Suministros de oficina",
                          "isActive": true,
                          "productCount": 3
                        }
                      ]
                    }
                    """
                )
            )
        )
    })
    public @interface GetAllCategoriesDoc {}

    // ============================================
    // GET /api/categories/active - Solo Activas
    // ============================================
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

    // ============================================
    // GET /api/categories/{id} - Obtener por ID
    // ============================================
    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(
        summary = "Obtener categoría por ID",
        description = "Obtiene los detalles de una categoría específica"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Categoría encontrada",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    value = """
                    {
                      "success": true,
                      "message": "Categoría obtenida exitosamente",
                      "data": {
                        "id": 1,
                        "name": "Electrónica",
                        "description": "Dispositivos electrónicos y tecnología",
                        "isActive": true,
                        "productCount": 5
                      }
                    }
                    """
                )
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Categoría no encontrada",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    value = """
                    {
                      "success": false,
                      "message": "No se encontró la categoría con ID: 999"
                    }
                    """
                )
            )
        )
    })
    public @interface GetCategoryByIdDoc {}

    // ============================================
    // PUT /api/categories/{id} - Actualizar
    // ============================================
    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(
        summary = "Actualizar categoría",
        description = "Actualiza los datos de una categoría existente"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Categoría actualizada exitosamente",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    value = """
                    {
                      "success": true,
                      "message": "Categoría actualizada exitosamente",
                      "data": {
                        "id": 1,
                        "name": "Electrónica y Tecnología",
                        "description": "Dispositivos electrónicos, computadoras y accesorios",
                        "isActive": true,
                        "productCount": 5
                      }
                    }
                    """
                )
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Categoría no encontrada"
        ),
        @ApiResponse(
            responseCode = "409",
            description = "El nuevo nombre ya existe"
        )
    })
    public @interface UpdateCategoryDoc {}

    // ============================================
    // DELETE /api/categories/{id} - Desactivar
    // ============================================
    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(
        summary = "Desactivar categoría",
        description = "Desactiva una categoría (soft delete). No la elimina físicamente, solo marca isActive = false."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Categoría desactivada exitosamente",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    value = """
                    {
                      "success": true,
                      "message": "Categoría desactivada exitosamente"
                    }
                    """
                )
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Categoría no encontrada"
        )
    })
    public @interface DeactivateCategoryDoc {}

    // ============================================
    // PATCH /api/categories/{id}/activate - Activar
    // ============================================
    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(
        summary = "Activar categoría",
        description = "Reactiva una categoría previamente desactivada"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Categoría activada exitosamente",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    value = """
                    {
                      "success": true,
                      "message": "Categoría activada exitosamente",
                      "data": {
                        "id": 1,
                        "name": "Electrónica",
                        "description": "Dispositivos electrónicos",
                        "isActive": true,
                        "productCount": 5
                      }
                    }
                    """
                )
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Categoría no encontrada"
        )
    })
    public @interface ActivateCategoryDoc {}

    // ============================================
    // DELETE /api/categories/{id}/soft - Eliminar (soft delete)
    // ============================================
    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(
        summary = "Eliminar categoría (soft delete)",
        description = "Marca la categoría como eliminada sin borrarla físicamente. Se guarda en el historial."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Categoría eliminada exitosamente",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    value = """
                    {
                      "success": true,
                      "message": "Categoría eliminada exitosamente"
                    }
                    """
                )
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Categoría no encontrada"
        ),
        @ApiResponse(
            responseCode = "400",
            description = "La categoría ya está eliminada"
        )
    })
    public @interface DeleteCategoryDoc {}

    // ============================================
    // GET /api/categories/deleted - Listar eliminadas
    // ============================================
    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(
        summary = "Listar categorías eliminadas",
        description = "Retorna el historial de categorías eliminadas (soft delete)"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Lista de categorías eliminadas obtenida exitosamente",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    value = """
                    {
                      "success": true,
                      "message": "Categorías eliminadas obtenidas exitosamente",
                      "data": [
                        {
                          "id": 5,
                          "name": "Categoría obsoleta",
                          "description": "Ya no se usa",
                          "isActive": false,
                          "productCount": 0
                        }
                      ]
                    }
                    """
                )
            )
        )
    })
    public @interface GetDeletedCategoriesDoc {}

    // ============================================
    // PATCH /api/categories/{id}/restore - Restaurar
    // ============================================
    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(
        summary = "Restaurar categoría eliminada",
        description = "Recupera una categoría previamente eliminada (soft delete)"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Categoría restaurada exitosamente",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    value = """
                    {
                      "success": true,
                      "message": "Categoría restaurada exitosamente",
                      "data": {
                        "id": 5,
                        "name": "Categoría restaurada",
                        "description": "Restaurada desde eliminados",
                        "isActive": false,
                        "productCount": 0
                      }
                    }
                    """
                )
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Categoría no encontrada"
        ),
        @ApiResponse(
            responseCode = "400",
            description = "La categoría no está eliminada"
        )
    })
    public @interface RestoreCategoryDoc {}

    // ============================================
    // DELETE /api/categories/{id}/permanent - Eliminar permanentemente
    // ============================================
    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(
        summary = "Eliminar categoría permanentemente",
        description = "Elimina físicamente la categoría de la base de datos. Esta acción es irreversible."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Categoría eliminada permanentemente",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    value = """
                    {
                      "success": true,
                      "message": "Categoría eliminada permanentemente"
                    }
                    """
                )
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Categoría no encontrada"
        )
    })
    public @interface PermanentDeleteCategoryDoc {}

    // ============================================
    // Parámetros Comunes
    // ============================================
    @Target(ElementType.PARAMETER)
    @Retention(RetentionPolicy.RUNTIME)
    @Parameter(
        name = "id",
        description = "ID único de la categoría",
        required = true,
        example = "1",
        schema = @Schema(type = "integer", format = "int64")
    )
    public @interface CategoryIdParam {}
}

