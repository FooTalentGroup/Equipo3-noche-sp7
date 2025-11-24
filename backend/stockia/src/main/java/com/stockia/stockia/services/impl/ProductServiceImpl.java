package com.stockia.stockia.services.Impl;

import com.stockia.stockia.dtos.product.ProductRequestDto;
import com.stockia.stockia.dtos.product.ProductResponseDto;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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
    public List<ProductResponseDto> getAllActiveProducts() {
        log.info("Fetching all active products");
        // Solo productos disponibles (isAvailable=true) y no eliminados (deleted=false)
        List<Product> products = productRepository.findByDeletedFalse().stream()
                .filter(Product::getIsAvailable)
                .toList();
        return productMapper.toResponseDtoList(products);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponseDto> getProductsWithFilters(Boolean includeInactive, Boolean lowStock) {
        log.info("Fetching products with filters - includeInactive: {}, lowStock: {}", includeInactive, lowStock);
        List<Product> products;

        // Paso 1: Filtrar por disponibilidad (includeInactive)
        if (includeInactive != null && includeInactive) {
            // Incluye productos activos e inactivos (isAvailable=true/false), pero NO
            // eliminados
            products = productRepository.findByDeletedFalse();
        } else {
            // Solo productos activos (isAvailable=true) y no eliminados
            products = productRepository.findByDeletedFalse().stream()
                    .filter(Product::getIsAvailable)
                    .toList();
        }

        // Paso 2: Filtrar por stock bajo (lowStock)
        if (lowStock != null && lowStock) {
            products = products.stream()
                    .filter(Product::hasLowStock)
                    .toList();
        }

        return productMapper.toResponseDtoList(products);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponseDto> searchProducts(String name, UUID categoryId) {
        log.info("Searching products: name={}, categoryId={}", name, categoryId);
        List<Product> products;
        if (name != null && categoryId != null) {
            products = productRepository.findByNameContainingIgnoreCaseAndCategoryIdAndDeletedFalse(name, categoryId);
        } else if (name != null) {
            products = productRepository.findByNameContainingIgnoreCaseAndDeletedFalse(name);
        } else if (categoryId != null) {
            products = productRepository.findByCategoryIdAndDeletedFalse(categoryId);
        } else {
            products = productRepository.findByDeletedFalse();
        }

        // Filtrar solo productos disponibles (isAvailable=true)
        products = products.stream()
                .filter(Product::getIsAvailable)
                .toList();

        return productMapper.toResponseDtoList(products);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponseDto> searchProductsWithManagementFilters(String query, UUID categoryId,
            Boolean includeInactive, Boolean lowStock) {
        log.info(
                "Searching products with management filters - query: {}, categoryId: {}, includeInactive: {}, lowStock: {}",
                query, categoryId, includeInactive, lowStock);

        // Paso 1: Búsqueda base
        List<Product> products;
        if (query != null && categoryId != null) {
            products = productRepository.findByNameContainingIgnoreCaseAndCategoryIdAndDeletedFalse(query, categoryId);
        } else if (query != null) {
            products = productRepository.findByNameContainingIgnoreCaseAndDeletedFalse(query);
        } else if (categoryId != null) {
            products = productRepository.findByCategoryIdAndDeletedFalse(categoryId);
        } else {
            products = productRepository.findByDeletedFalse();
        }

        // Paso 2: Aplicar filtros de gestión
        // Filtrar por disponibilidad
        if (includeInactive == null || !includeInactive) {
            products = products.stream()
                    .filter(Product::getIsAvailable)
                    .toList();
        }

        // Filtrar por stock bajo
        if (lowStock != null && lowStock) {
            products = products.stream()
                    .filter(Product::hasLowStock)
                    .toList();
        }

        return productMapper.toResponseDtoList(products);
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
                // Establecer el nombre normalizado en el DTO antes de mapear
                dto.setName(normalizedName);
            }
        }

        productMapper.updateEntityFromDto(product, dto);
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
    @Transactional(readOnly = true)
    public List<ProductResponseDto> getDeletedProducts() {
        log.info("Fetching deleted products");
        List<Product> products = productRepository.findByDeletedTrue();
        return productMapper.toResponseDtoList(products);
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
