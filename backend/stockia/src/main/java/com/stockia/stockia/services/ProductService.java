package com.stockia.stockia.services;

import com.stockia.stockia.dtos.product.ProductRequestDto;
import com.stockia.stockia.dtos.product.ProductResponseDto;
import com.stockia.stockia.dtos.product.ProductUpdateDto;

import java.util.List;

public interface ProductService {

    ProductResponseDto createProduct(ProductRequestDto dto);
    ProductResponseDto getProductById(Long id);
    List<ProductResponseDto> getAllActiveProducts();
    List<ProductResponseDto> getProductsWithFilters(Boolean includeInactive, Boolean lowStock);
    List<ProductResponseDto> searchProducts(String name, Long categoryId);
    List<ProductResponseDto> searchProductsWithManagementFilters(String query, Long categoryId, Boolean includeInactive, Boolean lowStock);
    ProductResponseDto updateProduct(Long id, ProductUpdateDto dto);
    void deleteProduct(Long id);
    List<ProductResponseDto> getDeletedProducts();
    ProductResponseDto restoreProduct(Long id);
    void permanentlyDeleteProduct(Long id);
}

