package com.stockia.stockia.services.Impl;

import com.stockia.stockia.dtos.category.CategoryRequestDto;
import com.stockia.stockia.dtos.category.CategoryResponseDto;
import com.stockia.stockia.dtos.category.CategorySearchRequestDto;
import com.stockia.stockia.dtos.category.CategoryUpdateDto;
import com.stockia.stockia.exceptions.category.CategoryNotFoundException;
import com.stockia.stockia.exceptions.category.DuplicateCategoryException;
import com.stockia.stockia.mappers.CategoryMapper;
import com.stockia.stockia.models.ProductCategory;
import com.stockia.stockia.repositories.ProductCategoryRepository;
import com.stockia.stockia.services.CategoryService;
import com.stockia.stockia.utils.SoftDeletableValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Implementación del servicio de gestión de categorías.
 *
 * @author StockIA Team
 * @version 1.0
 * @since 2025-11-20
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryServiceImpl implements CategoryService {

    private final ProductCategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    @Transactional
    public CategoryResponseDto createCategory(CategoryRequestDto dto) {
        log.info("Creando nueva categoría: {}", dto.getName());

        // Normalizar nombre a lowercase para consistencia
        String normalizedName = dto.getName().trim().toLowerCase();

        // Validar que no exista una categoría con el mismo nombre
        categoryRepository.findByNameIgnoreCase(normalizedName)
                .ifPresent(existing -> {
                    throw new DuplicateCategoryException(
                            "Ya existe una categoría con el nombre: " + normalizedName);
                });

        // Crear y guardar la categoría
        ProductCategory category = categoryMapper.toEntity(dto);
        category.setName(normalizedName); // Establecer el nombre normalizado
        ProductCategory savedCategory = categoryRepository.save(category);

        log.info("Categoría creada exitosamente con ID: {}", savedCategory.getId());
        return categoryMapper.toResponseDto(savedCategory);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CategoryResponseDto> searchCategories(CategorySearchRequestDto params, Pageable pageable) {
        log.debug("Buscando categorías con filtros: name={}, isActive={}, deleted={}",
                params.name(), params.isActive(), params.deleted());

        Page<ProductCategory> categories = categoryRepository.searchCategories(
                params.name(),
                params.isActive(),
                params.deleted(),
                pageable
        );

        return categories.map(categoryMapper::toResponseDto);
    }

    @Override
    @Transactional
    public CategoryResponseDto updateCategory(UUID id, CategoryUpdateDto dto) {
        log.info("Actualizando categoría con ID: {}", id);

        // Buscar la categoría existente
        ProductCategory category = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException(
                        "No se encontró la categoría con ID: " + id));

        // Validar y normalizar nombre único si se está cambiando
        if (dto.getName() != null && !dto.getName().trim().isEmpty()) {
            String normalizedName = dto.getName().trim().toLowerCase();

            // Solo validar si el nombre normalizado es diferente al actual
            if (!normalizedName.equals(category.getName())) {
                categoryRepository.findByNameIgnoreCase(normalizedName)
                        .ifPresent(existing -> {
                            throw new DuplicateCategoryException(
                                    "Ya existe una categoría con el nombre: " + normalizedName);
                        });
                // Establecer el nombre normalizado
                category.setName(normalizedName);
            }
        }

        // Actualizar solo los demás campos proporcionados (excepto nombre que ya se actualizó)
        if (dto.getDescription() != null) {
            category.setDescription(dto.getDescription());
        }
        if (dto.getIsActive() != null) {
            category.setIsActive(dto.getIsActive());
        }

        ProductCategory updatedCategory = categoryRepository.save(category);

        log.info("Categoría actualizada exitosamente: {}", updatedCategory.getName());
        return categoryMapper.toResponseDto(updatedCategory);
    }

    @Override
    @Transactional
    public void deleteCategory(UUID id) {
        log.info("Eliminando categoría con ID: {}", id);

        ProductCategory category = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException(
                        "No se encontró la categoría con ID: " + id));

        if (category.getDeleted()) {
            log.warn("Categoría con ID {} ya está eliminada", id);
            throw new IllegalStateException("La categoría ya está eliminada");
        }

        category.markAsDeleted();
        categoryRepository.save(category);

        log.info("Categoría eliminada exitosamente: {}", category.getName());
    }

    @Override
    @Transactional
    public CategoryResponseDto restoreCategory(UUID id) {
        log.info("Restaurando categoría con ID: {}", id);

        ProductCategory category = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException(
                        "No se encontró la categoría con ID: " + id));

        // Validar que esté eliminada antes de restaurar
        SoftDeletableValidator.validateIsDeleted(category.getDeleted(), "categoría", id);

        category.restore();
        ProductCategory restoredCategory = categoryRepository.save(category);

        log.info("Categoría restaurada exitosamente: {}", restoredCategory.getName());
        return categoryMapper.toResponseDto(restoredCategory);
    }

    @Override
    @Transactional
    public void permanentlyDeleteCategory(UUID id) {
        log.info("Eliminando permanentemente categoría con ID: {}", id);

        ProductCategory category = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException(
                        "No se encontró la categoría con ID: " + id));

        categoryRepository.delete(category);

        log.info("Categoría eliminada permanentemente con ID: {}", id);
    }
}
