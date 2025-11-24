package com.stockia.stockia.services;

import com.stockia.stockia.dtos.product.ProductRequestDto;
import com.stockia.stockia.dtos.product.ProductResponseDto;
import com.stockia.stockia.dtos.product.ProductUpdateDto;

import java.util.List;
import java.util.UUID;

public interface ProductService {

    ProductResponseDto createProduct(ProductRequestDto dto);

    ProductResponseDto getProductById(UUID id);

    List<ProductResponseDto> getAllActiveProducts();

    List<ProductResponseDto> getProductsWithFilters(Boolean includeInactive, Boolean lowStock);

    List<ProductResponseDto> searchProducts(String name, UUID categoryId);

    List<ProductResponseDto> searchProductsWithManagementFilters(String query, UUID categoryId, Boolean includeInactive,
            Boolean lowStock);

    ProductResponseDto updateProduct(UUID id, ProductUpdateDto dto);

    void deleteProduct(UUID id);

    List<ProductResponseDto> getDeletedProducts();

    ProductResponseDto restoreProduct(UUID id);

    void permanentlyDeleteProduct(UUID id);
}
