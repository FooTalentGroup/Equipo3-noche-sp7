package com.stockia.stockia.mappers;

import com.stockia.stockia.dtos.category.CategoryRequestDto;
import com.stockia.stockia.dtos.category.CategoryResponseDto;
import com.stockia.stockia.dtos.category.CategoryUpdateDto;
import com.stockia.stockia.models.ProductCategory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;


@Component
public class CategoryMapper {

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

    public List<CategoryResponseDto> toResponseDtoList(List<ProductCategory> categories) {
        if (categories == null) {
            return List.of();
        }

        return categories.stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

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
