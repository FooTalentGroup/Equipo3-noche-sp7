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

@CategoryControllerTag
@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

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
