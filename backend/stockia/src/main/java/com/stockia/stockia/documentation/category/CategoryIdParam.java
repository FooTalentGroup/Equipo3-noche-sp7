package com.stockia.stockia.documentation.category;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Parámetro común para el ID de categoría.
 *
 * @author StockIA Team
 * @version 1.0
 * @since 2025-11-22
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Parameter(
    name = "id",
    description = "ID único de la categoría",
    required = true,
    example = "1",
    schema = @Schema(type = "integer", format = "int64")
)
public @interface CategoryIdParam {}
