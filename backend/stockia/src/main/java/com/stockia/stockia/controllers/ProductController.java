package com.stockia.stockia.controllers;

import com.stockia.stockia.documentation.product.*;
import com.stockia.stockia.dtos.product.ProductRequestDto;
import com.stockia.stockia.dtos.product.ProductResponseDto;
import com.stockia.stockia.dtos.product.ProductUpdateDto;
import com.stockia.stockia.services.ProductService;
import com.stockia.stockia.utils.ApiResult;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@ProductControllerTag
@Slf4j
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    @CreateProductDoc
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResult<ProductResponseDto>> createProduct(@Valid @RequestBody ProductRequestDto dto) {
        log.info("POST /api/products - Creating product: {}", dto.getName());
        ProductResponseDto product = productService.createProduct(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .header("Location", "/api/products/" + product.getId())
                .body(ApiResult.success("Producto registrado exitosamente", product));
    }

    @GetMapping("/{id}")
    @GetProductByIdDoc
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResult<ProductResponseDto>> getProductById(@ProductIdParam @PathVariable Long id) {
        log.info("GET /api/products/{} - Fetching product", id);
        ProductResponseDto product = productService.getProductById(id);
        return ResponseEntity.ok(ApiResult.success("Producto encontrado", product));
    }

    @GetMapping
    @GetAllProductsDoc
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResult<List<ProductResponseDto>>> getAllProducts(
            @RequestParam(required = false) String q,
            @RequestParam(required = false) Long categoryId) {
        log.info("GET /api/products - q: {}, categoryId: {}", q, categoryId);

        // Si hay parámetros de búsqueda, usar el método de búsqueda
        if (q != null || categoryId != null) {
            List<ProductResponseDto> products = productService.searchProducts(q, categoryId);
            String message = products.isEmpty() ? "No se encontraron productos"
                    : products.size() + " producto(s) encontrado(s)";
            return ResponseEntity.ok(ApiResult.success(message, products));
        }

        // Si no hay parámetros, listar todos los activos
        List<ProductResponseDto> products = productService.getAllActiveProducts();
        String message = products.isEmpty() ? "No hay productos registrados"
                : products.size() + " producto(s) encontrado(s)";
        return ResponseEntity.ok(ApiResult.success(message, products));
    }

    @GetMapping("/management")
    @GetProductsManagementDoc
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResult<List<ProductResponseDto>>> getProductsForManagement(
            @IncludeInactiveParam @RequestParam(required = false, defaultValue = "false") Boolean includeInactive,
            @LowStockParam @RequestParam(required = false, defaultValue = "false") Boolean lowStock,
            @RequestParam(required = false) String q,
            @RequestParam(required = false) Long categoryId) {
        log.info("GET /api/products/management - includeInactive: {}, lowStock: {}, q: {}, categoryId: {}",
                includeInactive, lowStock, q, categoryId);

        List<ProductResponseDto> products;

        // Si hay parámetros de búsqueda, aplicar filtros de gestión sobre la búsqueda
        if (q != null || categoryId != null) {
            products = productService.searchProductsWithManagementFilters(q, categoryId, includeInactive, lowStock);
        } else {
            products = productService.getProductsWithFilters(includeInactive, lowStock);
        }

        String message = products.isEmpty() ? "No hay productos registrados"
                : products.size() + " producto(s) encontrado(s)";
        return ResponseEntity.ok(ApiResult.success(message, products));
    }

    @PutMapping("/{id}")
    @UpdateProductDoc
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResult<ProductResponseDto>> updateProduct(
            @ProductIdParam @PathVariable Long id, @Valid @RequestBody ProductUpdateDto dto) {
        log.info("PUT /api/products/{} - Updating product", id);
        ProductResponseDto product = productService.updateProduct(id, dto);
        return ResponseEntity.ok(ApiResult.success("Producto actualizado exitosamente", product));
    }

    @DeleteMapping("/{id}")
    @DeleteProductDoc
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResult<Void>> deleteProduct(@ProductIdParam @PathVariable Long id) {
        log.info("DELETE /api/products/{} - Deleting product", id);
        productService.deleteProduct(id);
        return ResponseEntity.ok(ApiResult.success("Producto eliminado exitosamente"));
    }

    @GetMapping("/deleted")
    @GetDeletedProductsDoc
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResult<List<ProductResponseDto>>> getDeletedProducts() {
        log.info("GET /api/products/deleted - Fetching deleted products");
        List<ProductResponseDto> products = productService.getDeletedProducts();
        String message = products.isEmpty() ? "No hay productos eliminados"
                : products.size() + " producto(s) eliminado(s)";
        return ResponseEntity.ok(ApiResult.success(message, products));
    }

    @PatchMapping("/{id}/restore")
    @RestoreProductDoc
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResult<ProductResponseDto>> restoreProduct(@ProductIdParam @PathVariable Long id) {
        log.info("PATCH /api/products/{}/restore - Restoring product", id);
        ProductResponseDto product = productService.restoreProduct(id);
        return ResponseEntity.ok(ApiResult.success("Producto restaurado exitosamente", product));
    }

    @DeleteMapping("/{id}/permanent")
    @PermanentDeleteProductDoc
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResult<Void>> permanentlyDeleteProduct(@ProductIdParam @PathVariable Long id) {
        log.info("DELETE /api/products/{}/permanent - Permanently deleting product", id);
        productService.permanentlyDeleteProduct(id);
        return ResponseEntity.ok(ApiResult.success("Producto eliminado permanentemente"));
    }
}

