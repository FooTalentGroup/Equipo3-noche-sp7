package com.stockia.stockia.documentation.category;

import io.swagger.v3.oas.annotations.tags.Tag;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Tag común para todos los endpoints de categorías.
 *
 * @author StockIA Team
 * @version 1.0
 * @since 2025-11-22
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Tag(
    name = "Categorías",
    description = "Gestión de categorías de productos. Permite crear, consultar, actualizar y desactivar categorías."
)
public @interface CategoryControllerTag {}

