package com.stockia.stockia.documentation.product;

import io.swagger.v3.oas.annotations.Parameter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Documentación del parámetro para incluir productos inactivos.
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Parameter(description = "Incluir productos inactivos (isAvailable=false). Por defecto false (solo activos)", example = "false")
public @interface IncludeInactiveParam {
}
