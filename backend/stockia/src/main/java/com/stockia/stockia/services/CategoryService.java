package com.stockia.stockia.services;

import com.stockia.stockia.dtos.category.CategoryRequestDto;
import com.stockia.stockia.dtos.category.CategoryResponseDto;
import com.stockia.stockia.dtos.category.CategorySearchRequestDto;
import com.stockia.stockia.dtos.category.CategoryUpdateDto;
import com.stockia.stockia.exceptions.category.CategoryNotFoundException;
import com.stockia.stockia.exceptions.category.DuplicateCategoryException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

/**
 * Interfaz de servicio para la gestión de categorías de productos.
 */
public interface CategoryService {

    CategoryResponseDto createCategory(CategoryRequestDto dto);

    Page<CategoryResponseDto> searchCategories(CategorySearchRequestDto params, Pageable pageable);

    CategoryResponseDto updateCategory(UUID id, CategoryUpdateDto dto);

    void deleteCategory(UUID id);

    CategoryResponseDto restoreCategory(UUID id);

    void permanentlyDeleteCategory(UUID id);
}
