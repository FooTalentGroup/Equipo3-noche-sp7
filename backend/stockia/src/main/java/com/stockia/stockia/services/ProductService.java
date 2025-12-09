package com.stockia.stockia.services;

import com.stockia.stockia.dtos.product.ProductRequestDto;
import com.stockia.stockia.dtos.product.ProductResponseDto;
import com.stockia.stockia.dtos.product.ProductSearchRequestDto;
import com.stockia.stockia.dtos.product.ProductUpdateDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ProductService {

    ProductResponseDto createProduct(ProductRequestDto dto);

    ProductResponseDto getProductById(UUID id);

    Page<ProductResponseDto> searchProducts(ProductSearchRequestDto params, Pageable pageable);

    ProductResponseDto updateProduct(UUID id, ProductUpdateDto dto);

    void deleteProduct(UUID id);


    ProductResponseDto restoreProduct(UUID id);

    void permanentlyDeleteProduct(UUID id);
}
