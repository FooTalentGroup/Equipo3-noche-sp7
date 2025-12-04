package com.stockia.stockia.documentation.product;

import io.swagger.v3.oas.annotations.Parameter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Documentación del parámetro para incluir productos inactivos.
 *
 * @author StockIA Team
 * @version 1.1
 * @since 2025-11-22
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Parameter(
    description = "Incluir productos inactivos (isAvailable=false). Por defecto false (solo activos)",
    example = "false"
)
public @interface IncludeInactiveParam {
}

