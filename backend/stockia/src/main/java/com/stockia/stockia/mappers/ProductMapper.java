package com.stockia.stockia.mappers;

import com.stockia.stockia.dtos.category.CategoryResponseDto;
import com.stockia.stockia.dtos.product.ProductRequestDto;
import com.stockia.stockia.dtos.product.ProductResponseDto;
import com.stockia.stockia.dtos.product.ProductUpdateDto;
import com.stockia.stockia.models.Product;
import com.stockia.stockia.models.ProductCategory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProductMapper {

    public Product toEntity(ProductRequestDto dto) {
        if (dto == null) {
            return null;
        }
        return Product.builder()
                .name(dto.getName())
                .price(dto.getPrice())
                .photoUrl(dto.getPhotoUrl())
                .currentStock(0) // Siempre 0 al crear
                .minStock(dto.getMinStock() != null ? dto.getMinStock() : 5) // 5 por defecto
                .isAvailable(true)
                .deleted(false)
                .build();
    }

    public ProductResponseDto toResponseDto(Product entity) {
        if (entity == null) {
            return null;
        }
        return ProductResponseDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .category(toCategoryDto(entity.getCategory()))
                .price(entity.getPrice())
                .photoUrl(entity.getPhotoUrl())
                .currentStock(entity.getCurrentStock())
                .minStock(entity.getMinStock())
                .isAvailable(entity.getIsAvailable())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .hasLowStock(entity.hasLowStock())
                .build();
    }

    public List<ProductResponseDto> toResponseDtoList(List<Product> entities) {
        if (entities == null) {
            return null;
        }
        return entities.stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    public CategoryResponseDto toCategoryDto(ProductCategory category) {
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

    public void updateEntityFromDto(Product entity, ProductUpdateDto dto) {
        if (entity == null || dto == null) {
            return;
        }
        if (dto.getName() != null) {
            entity.setName(dto.getName());
        }
        if (dto.getPrice() != null) {
            entity.setPrice(dto.getPrice());
        }
        if (dto.getPhotoUrl() != null) {
            entity.setPhotoUrl(dto.getPhotoUrl());
        }
        if (dto.getCurrentStock() != null) {
            entity.setCurrentStock(dto.getCurrentStock());
        }
        if (dto.getMinStock() != null) {
            entity.setMinStock(dto.getMinStock());
        }
        if (dto.getIsAvailable() != null) {
            entity.setIsAvailable(dto.getIsAvailable());
        }
    }
}

