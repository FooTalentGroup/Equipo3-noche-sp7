package com.stockia.stockia.documentation.category;

import io.swagger.v3.oas.annotations.tags.Tag;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Tag común para todos los endpoints de categorías.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Tag(name = "03 - Categorías", description = "Gestión de categorías de productos. Permite crear, consultar, actualizar y desactivar categorías.")
public @interface CategoryControllerTag {
}
