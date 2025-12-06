package com.stockia.stockia.documentation.product;

import io.swagger.v3.oas.annotations.Parameter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Documentación del parámetro ID de producto.
 *
 * @author StockIA Team
 * @version 1.1
 * @since 2025-11-22
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Parameter(description = "ID del producto", example = "123e4567-e89b-12d3-a456-426614174000")
public @interface ProductIdParam {
}
