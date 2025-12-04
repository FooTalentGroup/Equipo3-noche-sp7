package com.stockia.stockia.documentation.product;

import io.swagger.v3.oas.annotations.Parameter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Documentación del parámetro para filtrar productos con stock bajo.
 *
 * @author StockIA Team
 * @version 1.1
 * @since 2025-11-22
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Parameter(
    description = "Filtrar solo productos con stock bajo (currentStock <= minStock). Por defecto false (todos)",
    example = "false"
)
public @interface LowStockParam {
}

