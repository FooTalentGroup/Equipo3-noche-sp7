package com.stockia.stockia.services.impl;

import com.stockia.stockia.dtos.category.CategoryRequestDto;
import com.stockia.stockia.dtos.category.CategoryResponseDto;
import com.stockia.stockia.exceptions.category.CategoryNotFoundException;
import com.stockia.stockia.exceptions.category.DuplicateCategoryException;
import com.stockia.stockia.mappers.CategoryMapper;
import com.stockia.stockia.models.ProductCategory;
import com.stockia.stockia.repositories.ProductCategoryRepository;
import com.stockia.stockia.services.CategoryService;
import com.stockia.stockia.utils.SoftDeletableValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

        // Validar que no exista una categoría con el mismo nombre
        categoryRepository.findByNameIgnoreCase(dto.getName())
                .ifPresent(existing -> {
                    throw new DuplicateCategoryException(
                            "Ya existe una categoría con el nombre: " + dto.getName()
                    );
                });

        // Crear y guardar la categoría
        ProductCategory category = categoryMapper.toEntity(dto);
        ProductCategory savedCategory = categoryRepository.save(category);

        log.info("Categoría creada exitosamente con ID: {}", savedCategory.getId());
        return categoryMapper.toResponseDto(savedCategory);
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryResponseDto getCategoryById(Long id) {
        log.debug("Buscando categoría con ID: {}", id);

        ProductCategory category = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException(
                        "No se encontró la categoría con ID: " + id
                ));

        return categoryMapper.toResponseDto(category);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryResponseDto> getAllCategories() {
        log.debug("Listando todas las categorías");

        List<ProductCategory> categories = categoryRepository.findAll();
        return categoryMapper.toResponseDtoList(categories);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryResponseDto> getActiveCategories() {
        log.debug("Listando categorías activas");

        List<ProductCategory> categories = categoryRepository.findByIsActiveTrue();
        return categoryMapper.toResponseDtoList(categories);
    }

    @Override
    @Transactional
    public CategoryResponseDto updateCategory(Long id, CategoryRequestDto dto) {
        log.info("Actualizando categoría con ID: {}", id);

        // Buscar la categoría existente
        ProductCategory category = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException(
                        "No se encontró la categoría con ID: " + id
                ));

        // Validar nombre duplicado (si cambió el nombre)
        if (!category.getName().equalsIgnoreCase(dto.getName())) {
            categoryRepository.findByNameIgnoreCase(dto.getName())
                    .ifPresent(existing -> {
                        throw new DuplicateCategoryException(
                                "Ya existe una categoría con el nombre: " + dto.getName()
                        );
                    });
        }

        // Actualizar campos
        categoryMapper.updateEntityFromDto(category, dto);
        ProductCategory updatedCategory = categoryRepository.save(category);

        log.info("Categoría actualizada exitosamente: {}", updatedCategory.getName());
        return categoryMapper.toResponseDto(updatedCategory);
    }

    @Override
    @Transactional
    public void deactivateCategory(Long id) {
        log.info("Desactivando categoría con ID: {}", id);

        ProductCategory category = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException(
                        "No se encontró la categoría con ID: " + id
                ));

        category.setIsActive(false);
        categoryRepository.save(category);

        log.info("Categoría desactivada exitosamente: {}", category.getName());
    }

    @Override
    @Transactional
    public CategoryResponseDto activateCategory(Long id) {
        log.info("Activando categoría con ID: {}", id);

        ProductCategory category = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException(
                        "No se encontró la categoría con ID: " + id
                ));

        category.setIsActive(true);
        ProductCategory activatedCategory = categoryRepository.save(category);

        log.info("Categoría activada exitosamente: {}", activatedCategory.getName());
        return categoryMapper.toResponseDto(activatedCategory);
    }

    @Override
    @Transactional
    public void deleteCategory(Long id) {
        log.info("Eliminando categoría con ID: {}", id);

        ProductCategory category = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException(
                        "No se encontró la categoría con ID: " + id
                ));

        if (category.getDeleted()) {
            log.warn("Categoría con ID {} ya está eliminada", id);
            throw new IllegalStateException("La categoría ya está eliminada");
        }

        category.markAsDeleted();
        categoryRepository.save(category);

        log.info("Categoría eliminada exitosamente: {}", category.getName());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryResponseDto> getDeletedCategories() {
        log.debug("Listando categorías eliminadas");

        List<ProductCategory> categories = categoryRepository.findByDeletedTrue();
        return categoryMapper.toResponseDtoList(categories);
    }

    @Override
    @Transactional
    public CategoryResponseDto restoreCategory(Long id) {
        log.info("Restaurando categoría con ID: {}", id);

        ProductCategory category = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException(
                        "No se encontró la categoría con ID: " + id
                ));

        // Validar que esté eliminada antes de restaurar
        SoftDeletableValidator.validateIsDeleted(category.getDeleted(), "categoría", id);

        category.restore();
        ProductCategory restoredCategory = categoryRepository.save(category);

        log.info("Categoría restaurada exitosamente: {}", restoredCategory.getName());
        return categoryMapper.toResponseDto(restoredCategory);
    }

    @Override
    @Transactional
    public void permanentlyDeleteCategory(Long id) {
        log.info("Eliminando permanentemente categoría con ID: {}", id);

        ProductCategory category = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException(
                        "No se encontró la categoría con ID: " + id
                ));

        categoryRepository.delete(category);

        log.info("Categoría eliminada permanentemente con ID: {}", id);
    }
}

