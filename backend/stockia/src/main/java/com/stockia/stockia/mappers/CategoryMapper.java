package com.stockia.stockia.mappers;

import com.stockia.stockia.dtos.category.CategoryRequestDto;
import com.stockia.stockia.dtos.category.CategoryResponseDto;
import com.stockia.stockia.dtos.category.CategoryUpdateDto;
import com.stockia.stockia.models.ProductCategory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper para convertir entre entidades ProductCategory y DTOs.
 *
 * @author StockIA Team
 * @version 1.0
 * @since 2025-11-20
 */
@Component
public class CategoryMapper {

    /**
     * Convierte una entidad ProductCategory a DTO de respuesta.
     *
     * @param category Entidad ProductCategory
     * @return DTO de respuesta
     */
    public CategoryResponseDto toResponseDto(ProductCategory category) {
        if (category == null) {
            return null;
        }

        return CategoryResponseDto.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .isActive(category.getIsActive())
                .productCount(category.getProducts() != null ? category.getProducts().size() : 0)
                .build();
    }

    /**
     * Convierte una lista de entidades a lista de DTOs de respuesta.
     *
     * @param categories Lista de entidades ProductCategory
     * @return Lista de DTOs de respuesta
     */
    public List<CategoryResponseDto> toResponseDtoList(List<ProductCategory> categories) {
        if (categories == null) {
            return List.of();
        }

        return categories.stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    /**
     * Convierte un DTO de request a entidad ProductCategory.
     *
     * @param dto DTO de request
     * @return Entidad ProductCategory
     */
    public ProductCategory toEntity(CategoryRequestDto dto) {
        if (dto == null) {
            return null;
        }

        return ProductCategory.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .isActive(dto.getIsActive() != null ? dto.getIsActive() : true)
                .build();
    }

    /**
     * Actualiza una entidad existente con los datos del DTO (para creación/actualización completa).
     *
     * @param category Entidad existente
     * @param dto DTO con nuevos datos
     */
    public void updateEntityFromDto(ProductCategory category, CategoryRequestDto dto) {
        if (category == null || dto == null) {
            return;
        }

        if (dto.getName() != null) {
            category.setName(dto.getName());
        }
        if (dto.getDescription() != null) {
            category.setDescription(dto.getDescription());
        }
        if (dto.getIsActive() != null) {
            category.setIsActive(dto.getIsActive());
        }
    }

    /**
     * Actualiza una entidad existente con los datos del DTO (para actualización parcial).
     * Solo actualiza los campos que no son null.
     *
     * @param category Entidad existente
     * @param dto DTO con nuevos datos (todos opcionales)
     */
    public void updateEntityFromDto(ProductCategory category, CategoryUpdateDto dto) {
        if (category == null || dto == null) {
            return;
        }

        if (dto.getName() != null && !dto.getName().trim().isEmpty()) {
            category.setName(dto.getName());
        }
        if (dto.getDescription() != null) {
            category.setDescription(dto.getDescription());
        }
        if (dto.getIsActive() != null) {
            category.setIsActive(dto.getIsActive());
        }
    }
}

