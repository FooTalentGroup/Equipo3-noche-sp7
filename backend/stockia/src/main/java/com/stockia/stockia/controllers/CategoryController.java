package com.stockia.stockia.controllers;

import com.stockia.stockia.documentation.category.*;
import com.stockia.stockia.dtos.category.CategoryRequestDto;
import com.stockia.stockia.dtos.category.CategoryResponseDto;
import com.stockia.stockia.dtos.category.CategorySearchRequestDto;
import com.stockia.stockia.dtos.category.CategoryUpdateDto;
import com.stockia.stockia.services.CategoryService;
import com.stockia.stockia.utils.ApiResult;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static com.stockia.stockia.security.constants.SecurityConstants.Roles.*;

/**
 * Controlador REST para la gestión de categorías de productos.
 *
 * Endpoints disponibles:
 * 
 * - DELETE [/api/categories/{id}] → Eliminar categoría (soft delete)
 * - DELETE [/api/categories/{id}/permanent] → Eliminar categoría permanentemente
 * - GET [/api/categories] → Buscar y listar categorías con filtros (nombre, isActive, deleted)
 * - PATCH [/api/categories/{id}/restore] → Restaurar categoría eliminada
 * - PATCH [/api/categories/{id}/deactivate] → Desactivar categoría
 * - PATCH [/api/categories/{id}/activate] → Activar categoría
 * - POST [/api/categories] → Crear nueva categoría
 * - PUT [/api/categories/{id}] → Actualizar categoría
 *
 */
@CategoryControllerTag
@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
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
     * Busca y lista categorías con filtros opcionales.
     * Soporta filtrado por nombre, estado activo y estado de eliminación.
     * Soporta paginación y ordenamiento.
     *
     * @param params Parámetros de búsqueda (nombre, isActive, deleted)
     * @param pageable Configuración de paginación y ordenamiento
     * @return ResponseEntity con página de categorías (200)
     */
    @GetMapping
    @GetAllCategoriesDoc
    @PreAuthorize(ADMIN_OR_MANAGER)
    public ResponseEntity<ApiResult<Page<CategoryResponseDto>>> searchCategories(
            @ParameterObject @Valid CategorySearchRequestDto params,
            @ParameterObject Pageable pageable) {

        Page<CategoryResponseDto> categories = categoryService.searchCategories(params, pageable);

        return ResponseEntity.ok(
                ApiResult.success("Categorías obtenidas exitosamente", categories)
        );
    }

    /**
     * Actualiza una categoría existente (actualización parcial).
     * Solo se actualizan los campos proporcionados.
     *
     * @param id ID de la categoría a actualizar
     * @param dto Nuevos datos de la categoría (todos los campos son opcionales)
     * @return ResponseEntity con la categoría actualizada (200)
     */
    @PutMapping("/{id}")
    @UpdateCategoryDoc
    @PreAuthorize(ADMIN_ONLY)
    public ResponseEntity<ApiResult<CategoryResponseDto>> updateCategory(
            @PathVariable @CategoryIdParam UUID id,
            @Valid @RequestBody CategoryUpdateDto dto) {

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
