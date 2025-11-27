package com.stockia.stockia.services.Impl;

import com.stockia.stockia.dtos.product.ProductRequestDto;
import com.stockia.stockia.dtos.product.ProductResponseDto;
import com.stockia.stockia.dtos.product.ProductSearchRequestDto;
import com.stockia.stockia.dtos.product.ProductUpdateDto;
import com.stockia.stockia.exceptions.category.CategoryNotFoundException;
import com.stockia.stockia.exceptions.product.DuplicateProductException;
import com.stockia.stockia.exceptions.product.ProductNotFoundException;
import com.stockia.stockia.mappers.ProductMapper;
import com.stockia.stockia.models.Product;
import com.stockia.stockia.models.ProductCategory;
import com.stockia.stockia.repositories.ProductCategoryRepository;
import com.stockia.stockia.repositories.ProductRepository;
import com.stockia.stockia.services.ProductService;
import com.stockia.stockia.utils.SoftDeletableValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Slf4j
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductCategoryRepository categoryRepository;
    private final ProductMapper productMapper;

    public ProductServiceImpl(ProductRepository productRepository,
            ProductCategoryRepository categoryRepository,
            ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.productMapper = productMapper;
    }

    @Override
    @Transactional
    public ProductResponseDto createProduct(ProductRequestDto dto) {
        log.info("Creating product with name: {}", dto.getName());

        // Normalizar nombre a lowercase para consistencia
        String normalizedName = dto.getName().trim().toLowerCase();

        // Validar que la categoría exista
        ProductCategory category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new CategoryNotFoundException(dto.getCategoryId()));

        // Validar que el nombre sea único
        boolean exists = productRepository.existsActiveProductByName(normalizedName);
        if (exists) {
            log.warn("Duplicate product name: {}", normalizedName);
            throw new DuplicateProductException(
                    "Producto duplicado. Ya existe un producto con el nombre: " + normalizedName);
        }

        Product product = productMapper.toEntity(dto);
        product.setName(normalizedName); // Establecer el nombre normalizado
        product.setCategory(category);
        Product savedProduct = productRepository.save(product);
        log.info("Product created with ID: {}", savedProduct.getId());
        return productMapper.toResponseDto(savedProduct);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponseDto getProductById(UUID id) {
        log.info("Fetching product with ID: {}", id);
        Product product = productRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
        return productMapper.toResponseDto(product);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductResponseDto> searchProducts(ProductSearchRequestDto params, Pageable pageable) {
        log.info("Searching products with filters - deleted: {}, includeInactive: {}, lowStock: {}, query: {}, categoryId: {}",
                params.deleted(), params.includeInactive(), params.lowStock(), params.q(), params.categoryId());

        Page<Product> products = productRepository.searchProducts(
                params.q(),
                params.categoryId(),
                params.deleted(),
                params.includeInactive(),
                params.lowStock(),
                pageable
        );

        return products.map(productMapper::toResponseDto);
    }

    @Override
    @Transactional
    public ProductResponseDto updateProduct(UUID id, ProductUpdateDto dto) {
        log.info("Updating product with ID: {}", id);
        Product product = productRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ProductNotFoundException(id));

        // Actualizar categoría si se proporciona
        if (dto.getCategoryId() != null && !dto.getCategoryId().equals(product.getCategory().getId())) {
            ProductCategory newCategory = categoryRepository.findById(dto.getCategoryId())
                    .orElseThrow(() -> new CategoryNotFoundException(dto.getCategoryId()));
            product.setCategory(newCategory);
        }

        // Validar y normalizar nombre único si se está cambiando
        if (dto.getName() != null && !dto.getName().trim().isEmpty()) {
            String normalizedName = dto.getName().trim().toLowerCase();

            // Solo validar si el nombre normalizado es diferente al actual
            if (!normalizedName.equals(product.getName())) {
                boolean exists = productRepository.existsActiveProductByNameExcludingId(normalizedName, id);
                if (exists) {
                    throw new DuplicateProductException(
                            "Producto duplicado. Ya existe otro producto con el nombre: " + normalizedName);
                }
                // Establecer el nombre normalizado
                product.setName(normalizedName);
            }
        }

        // Actualizar los demás campos (price, photoUrl, stock, etc.)
        if (dto.getPrice() != null) {
            product.setPrice(dto.getPrice());
        }
        if (dto.getPhotoUrl() != null) {
            product.setPhotoUrl(dto.getPhotoUrl());
        }
        if (dto.getCurrentStock() != null) {
            product.setCurrentStock(dto.getCurrentStock());
        }
        if (dto.getMinStock() != null) {
            product.setMinStock(dto.getMinStock());
        }
        if (dto.getIsAvailable() != null) {
            product.setIsAvailable(dto.getIsAvailable());
        }

        Product updatedProduct = productRepository.save(product);
        log.info("Product updated with ID: {}", updatedProduct.getId());
        return productMapper.toResponseDto(updatedProduct);
    }

    @Override
    @Transactional
    public void deleteProduct(UUID id) {
        log.info("Deleting product with ID: {}", id);
        Product product = productRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
        product.markAsDeleted();
        productRepository.save(product);
        log.info("Product deleted with ID: {}", id);
    }


    @Override
    @Transactional
    public ProductResponseDto restoreProduct(UUID id) {
        log.info("Restoring product with ID: {}", id);
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));

        // Validar que esté eliminado antes de restaurar
        SoftDeletableValidator.validateIsDeleted(product.getDeleted(), "producto", id);

        product.restore();
        Product restoredProduct = productRepository.save(product);
        log.info("Product restored with ID: {}", id);
        return productMapper.toResponseDto(restoredProduct);
    }

    @Override
    @Transactional
    public void permanentlyDeleteProduct(UUID id) {
        log.info("Permanently deleting product with ID: {}", id);
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));

        productRepository.delete(product);
        log.info("Product permanently deleted with ID: {}", id);
    }
}
