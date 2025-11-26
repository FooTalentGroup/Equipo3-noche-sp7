package com.stockia.stockia.controllers;

import com.stockia.stockia.documentation.product.*;
import com.stockia.stockia.dtos.product.ProductRequestDto;
import com.stockia.stockia.dtos.product.ProductResponseDto;
import com.stockia.stockia.dtos.product.ProductSearchRequestDto;
import com.stockia.stockia.dtos.product.ProductUpdateDto;
import com.stockia.stockia.services.ProductService;
import com.stockia.stockia.utils.ApiResult;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static com.stockia.stockia.security.constants.SecurityConstants.Roles.*;

/**
 * Controlador REST para la gestión de productos.
 *
 * Endpoints disponibles:
 * - DELETE [/api/products/{id}] → Eliminar producto (soft delete)
 * - DELETE [/api/products/{id}/permanent] → Eliminar producto permanentemente
 * - GET [/api/products/{id}] → Obtener producto por ID
 * - GET [/api/products] → Listar todos los productos con paginación (filtros: deleted, includeInactive, lowStock, q, categoryId)
 * - PATCH [/api/products/{id}/restore] → Restaurar producto eliminado
 * - POST [/api/products] → Registrar nuevo producto
 * - PUT [/api/products/{id}] → Actualizar producto existente
 */
@ProductControllerTag
@RestController
@RequestMapping("/api/products")
@Slf4j
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    @CreateProductDoc
    @PreAuthorize(ADMIN_OR_MANAGER)
    public ResponseEntity<ApiResult<ProductResponseDto>> createProduct(@Valid @RequestBody ProductRequestDto dto) {
        log.info("POST /api/products - Creating product: {}", dto.getName());
        ProductResponseDto product = productService.createProduct(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .header("Location", "/api/products/" + product.getId())
                .body(ApiResult.success("Producto registrado exitosamente", product));
    }

    @GetMapping("/{id}")
    @GetProductByIdDoc
    @PreAuthorize(ADMIN_OR_MANAGER)
    public ResponseEntity<ApiResult<ProductResponseDto>> getProductById(@ProductIdParam @PathVariable UUID id) {
        log.info("GET /api/products/{} - Fetching product", id);
        ProductResponseDto product = productService.getProductById(id);
        return ResponseEntity.ok(ApiResult.success("Producto encontrado", product));
    }

    @GetMapping
    @GetAllProductsDoc
    @PreAuthorize(ADMIN_OR_MANAGER)
    public ResponseEntity<ApiResult<Page<ProductResponseDto>>> getAllProducts(
            @ParameterObject @Valid ProductSearchRequestDto params,
            @ParameterObject Pageable pageable) {
        log.info("GET /api/products - deleted: {}, includeInactive: {}, lowStock: {}, q: {}, categoryId: {}",
                params.deleted(), params.includeInactive(), params.lowStock(), params.q(), params.categoryId());

        Page<ProductResponseDto> products = productService.searchProducts(params, pageable);

        return ResponseEntity.ok(ApiResult.success("Productos obtenidos exitosamente", products));
    }

    @PutMapping("/{id}")
    @UpdateProductDoc
    @PreAuthorize(ADMIN_ONLY)
    public ResponseEntity<ApiResult<ProductResponseDto>> updateProduct(
            @ProductIdParam @PathVariable UUID id, @Valid @RequestBody ProductUpdateDto dto) {
        log.info("PUT /api/products/{} - Updating product", id);
        ProductResponseDto product = productService.updateProduct(id, dto);
        return ResponseEntity.ok(ApiResult.success("Producto actualizado exitosamente", product));
    }

    @DeleteMapping("/{id}")
    @DeleteProductDoc
    @PreAuthorize(ADMIN_ONLY)
    public ResponseEntity<ApiResult<Void>> deleteProduct(@ProductIdParam @PathVariable UUID id) {
        log.info("DELETE /api/products/{} - Deleting product", id);
        productService.deleteProduct(id);
        return ResponseEntity.ok(ApiResult.success("Producto eliminado exitosamente"));
    }

    @PatchMapping("/{id}/restore")
    @RestoreProductDoc
    @PreAuthorize(ADMIN_ONLY)
    public ResponseEntity<ApiResult<ProductResponseDto>> restoreProduct(@ProductIdParam @PathVariable UUID id) {
        log.info("PATCH /api/products/{}/restore - Restoring product", id);
        ProductResponseDto product = productService.restoreProduct(id);
        return ResponseEntity.ok(ApiResult.success("Producto restaurado exitosamente", product));
    }

    @DeleteMapping("/{id}/permanent")
    @PermanentDeleteProductDoc
    @PreAuthorize(ADMIN_ONLY)
    public ResponseEntity<ApiResult<Void>> permanentlyDeleteProduct(@ProductIdParam @PathVariable UUID id) {
        log.info("DELETE /api/products/{}/permanent - Permanently deleting product", id);
        productService.permanentlyDeleteProduct(id);
        return ResponseEntity.ok(ApiResult.success("Producto eliminado permanentemente"));
    }
}
