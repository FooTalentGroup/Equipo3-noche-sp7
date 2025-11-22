package com.stockia.stockia.controllers;

import com.stockia.stockia.documentation.CategoryApiDocumentation.*;
import com.stockia.stockia.dtos.category.CategoryRequestDto;
import com.stockia.stockia.dtos.category.CategoryResponseDto;
import com.stockia.stockia.services.CategoryService;
import com.stockia.stockia.utils.ApiResult;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para la gestión de categorías de productos.
 *
 * Endpoints disponibles:
 * - POST   /api/categories                  → Crear categoría
 * - GET    /api/categories                  → Listar todas
 * - GET    /api/categories/active           → Listar solo activas
 * - GET    /api/categories/deleted          → Listar eliminadas
 * - GET    /api/categories/{id}             → Obtener por ID
 * - PUT    /api/categories/{id}             → Actualizar
 * - DELETE /api/categories/{id}             → Eliminar (soft delete)
 * - PATCH  /api/categories/{id}/deactivate  → Desactivar
 * - PATCH  /api/categories/{id}/activate    → Activar
 * - PATCH  /api/categories/{id}/restore     → Restaurar eliminada
 * - DELETE /api/categories/{id}/permanent   → Eliminar permanentemente
 *
 * @author StockIA Team
 * @version 1.0
 * @since 2025-11-20
 */
@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
@CategoryControllerTag
public class CategoryController {

    private final CategoryService categoryService;

    /**
     * Crea una nueva categoría.
     *
     * @param dto Datos de la categoría a crear
     * @return ResponseEntity con la categoría creada (201)
     */
    @PostMapping
    @CreateCategoryDoc
    public ResponseEntity<ApiResult<CategoryResponseDto>> createCategory(
            @Valid @RequestBody CategoryRequestDto dto) {

        CategoryResponseDto category = categoryService.createCategory(dto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResult.success("Categoría creada exitosamente", category));
    }

    /**
     * Lista todas las categorías (activas e inactivas).
     *
     * @return ResponseEntity con lista de categorías (200)
     */
    @GetMapping
    @GetAllCategoriesDoc
    public ResponseEntity<ApiResult<List<CategoryResponseDto>>> getAllCategories() {

        List<CategoryResponseDto> categories = categoryService.getAllCategories();

        return ResponseEntity.ok(
                ApiResult.success("Categorías obtenidas exitosamente", categories)
        );
    }

    /**
     * Lista solo las categorías activas.
     *
     * @return ResponseEntity con lista de categorías activas (200)
     */
    @GetMapping("/active")
    @GetActiveCategoriesDoc
    public ResponseEntity<ApiResult<List<CategoryResponseDto>>> getActiveCategories() {

        List<CategoryResponseDto> categories = categoryService.getActiveCategories();

        return ResponseEntity.ok(
                ApiResult.success("Categorías activas obtenidas exitosamente", categories)
        );
    }

    /**
     * Obtiene una categoría por su ID.
     *
     * @param id ID de la categoría
     * @return ResponseEntity con la categoría encontrada (200)
     */
    @GetMapping("/{id}")
    @GetCategoryByIdDoc
    public ResponseEntity<ApiResult<CategoryResponseDto>> getCategoryById(
            @PathVariable @CategoryIdParam Long id) {

        CategoryResponseDto category = categoryService.getCategoryById(id);

        return ResponseEntity.ok(
                ApiResult.success("Categoría obtenida exitosamente", category)
        );
    }

    /**
     * Actualiza una categoría existente.
     *
     * @param id ID de la categoría a actualizar
     * @param dto Nuevos datos de la categoría
     * @return ResponseEntity con la categoría actualizada (200)
     */
    @PutMapping("/{id}")
    @UpdateCategoryDoc
    public ResponseEntity<ApiResult<CategoryResponseDto>> updateCategory(
            @PathVariable @CategoryIdParam Long id,
            @Valid @RequestBody CategoryRequestDto dto) {

        CategoryResponseDto category = categoryService.updateCategory(id, dto);

        return ResponseEntity.ok(
                ApiResult.success("Categoría actualizada exitosamente", category)
        );
    }

    /**
     * Elimina una categoría (soft delete).
     *
     * @param id ID de la categoría a eliminar
     * @return ResponseEntity con mensaje de éxito (200)
     */
    @DeleteMapping("/{id}")
    @DeleteCategoryDoc
    public ResponseEntity<ApiResult<Void>> deleteCategory(
            @PathVariable @CategoryIdParam Long id) {

        categoryService.deleteCategory(id);

        return ResponseEntity.ok(
                ApiResult.success("Categoría eliminada exitosamente")
        );
    }

    /**
     * Desactiva una categoría (marca como inactiva).
     *
     * @param id ID de la categoría a desactivar
     * @return ResponseEntity con mensaje de éxito (200)
     */
    @PatchMapping("/{id}/deactivate")
    @DeactivateCategoryDoc
    public ResponseEntity<ApiResult<Void>> deactivateCategory(
            @PathVariable @CategoryIdParam Long id) {

        categoryService.deactivateCategory(id);

        return ResponseEntity.ok(
                ApiResult.success("Categoría desactivada exitosamente")
        );
    }

    /**
     * Activa una categoría previamente desactivada.
     *
     * @param id ID de la categoría a activar
     * @return ResponseEntity con la categoría activada (200)
     */
    @PatchMapping("/{id}/activate")
    @ActivateCategoryDoc
    public ResponseEntity<ApiResult<CategoryResponseDto>> activateCategory(
            @PathVariable @CategoryIdParam Long id) {

        CategoryResponseDto category = categoryService.activateCategory(id);

        return ResponseEntity.ok(
                ApiResult.success("Categoría activada exitosamente", category)
        );
    }

    /**
     * Lista las categorías eliminadas.
     *
     * @return ResponseEntity con lista de categorías eliminadas (200)
     */
    @GetMapping("/deleted")
    @GetDeletedCategoriesDoc
    public ResponseEntity<ApiResult<List<CategoryResponseDto>>> getDeletedCategories() {

        List<CategoryResponseDto> categories = categoryService.getDeletedCategories();

        return ResponseEntity.ok(
                ApiResult.success("Categorías eliminadas obtenidas exitosamente", categories)
        );
    }

    /**
     * Restaura una categoría eliminada.
     *
     * @param id ID de la categoría a restaurar
     * @return ResponseEntity con la categoría restaurada (200)
     */
    @PatchMapping("/{id}/restore")
    @RestoreCategoryDoc
    public ResponseEntity<ApiResult<CategoryResponseDto>> restoreCategory(
            @PathVariable @CategoryIdParam Long id) {

        CategoryResponseDto category = categoryService.restoreCategory(id);

        return ResponseEntity.ok(
                ApiResult.success("Categoría restaurada exitosamente", category)
        );
    }

    /**
     * Elimina permanentemente una categoría.
     *
     * @param id ID de la categoría a eliminar permanentemente
     * @return ResponseEntity con mensaje de éxito (200)
     */
    @DeleteMapping("/{id}/permanent")
    @PermanentDeleteCategoryDoc
    public ResponseEntity<ApiResult<Void>> permanentlyDeleteCategory(
            @PathVariable @CategoryIdParam Long id) {

        categoryService.permanentlyDeleteCategory(id);

        return ResponseEntity.ok(
                ApiResult.success("Categoría eliminada permanentemente")
        );
    }
}

