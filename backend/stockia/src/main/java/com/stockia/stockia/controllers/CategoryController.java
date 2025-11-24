package com.stockia.stockia.controllers;

import com.stockia.stockia.documentation.category.CategoryControllerTag;
import com.stockia.stockia.documentation.category.CategoryIdParam;
import com.stockia.stockia.documentation.category.CreateCategoryDoc;
import com.stockia.stockia.documentation.category.GetAllCategoriesDoc;
import com.stockia.stockia.documentation.category.GetActiveCategoriesDoc;
import com.stockia.stockia.documentation.category.GetCategoryByIdDoc;
import com.stockia.stockia.documentation.category.GetDeletedCategoriesDoc;
import com.stockia.stockia.documentation.category.UpdateCategoryDoc;
import com.stockia.stockia.documentation.category.DeactivateCategoryDoc;
import com.stockia.stockia.documentation.category.ActivateCategoryDoc;
import com.stockia.stockia.documentation.category.DeleteCategoryDoc;
import com.stockia.stockia.documentation.category.RestoreCategoryDoc;
import com.stockia.stockia.documentation.category.PermanentDeleteCategoryDoc;
import com.stockia.stockia.dtos.category.CategoryRequestDto;
import com.stockia.stockia.dtos.category.CategoryResponseDto;
import com.stockia.stockia.services.CategoryService;
import com.stockia.stockia.utils.ApiResult;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static com.stockia.stockia.security.constants.SecurityConstants.Roles.*;

/**
 * Controlador REST para la gestión de categorías de productos.
 *
 * Endpoints disponibles:
 * 
 * - DELETE [/api/categories/{id}] → Eliminar categoría (soft delete)
 * - DELETE [/api/categories/{id}/permanent] → Eliminar categoría permanentemente
 * - GET [/api/categories/{id}] → Obtener categoría por ID
 * - GET [/api/categories] → Listar todas las categorías
 * - GET [/api/categories/deleted] → Listar categorías eliminadas
 * - GET [/api/categories/active] → Listar categorías activas
 * - PATCH [/api/categories/{id}/restore] → Restaurar categoría eliminada
 * - PATCH [/api/categories/{id}/deactivate] → Desactivar categoría
 * - PATCH [/api/categories/{id}/activate] → Activar categoría
 * - POST [/api/categories] → Crear nueva categoría
 * - PUT [/api/categories/{id} → Actualizar categoría
 * 
 * @author StockIA Team (Maidana)
 * @version 1.0
 * @since 2025-11-20
 */
@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
@Tag(name = "04 - Categorías", description = "Endpoints para la gestión de categorías")
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
    @PreAuthorize(ADMIN_OR_MANAGER)
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
    @PreAuthorize(ADMIN_OR_MANAGER)
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
    @PreAuthorize(ADMIN_OR_MANAGER)
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
    @PreAuthorize(ADMIN_OR_MANAGER)
    public ResponseEntity<ApiResult<CategoryResponseDto>> getCategoryById(
            @PathVariable @CategoryIdParam UUID id) {

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
    @PreAuthorize(ADMIN_ONLY)
    public ResponseEntity<ApiResult<CategoryResponseDto>> updateCategory(
            @PathVariable @CategoryIdParam UUID id,
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
    @PreAuthorize(ADMIN_ONLY)
    public ResponseEntity<ApiResult<Void>> deleteCategory(
            @PathVariable @CategoryIdParam UUID id) {

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
    @PreAuthorize(ADMIN_ONLY)
    public ResponseEntity<ApiResult<Void>> deactivateCategory(
            @PathVariable @CategoryIdParam UUID id) {

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
    @PreAuthorize(ADMIN_ONLY)
    public ResponseEntity<ApiResult<CategoryResponseDto>> activateCategory(
            @PathVariable @CategoryIdParam UUID id) {

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
    @PreAuthorize(ADMIN_ONLY)
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
    @PreAuthorize(ADMIN_ONLY)
    public ResponseEntity<ApiResult<CategoryResponseDto>> restoreCategory(
            @PathVariable @CategoryIdParam UUID id) {

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
    @PreAuthorize(ADMIN_ONLY)
    public ResponseEntity<ApiResult<Void>> permanentlyDeleteCategory(
            @PathVariable @CategoryIdParam UUID id) {

        categoryService.permanentlyDeleteCategory(id);

        return ResponseEntity.ok(
                ApiResult.success("Categoría eliminada permanentemente")
        );
    }
}
