package com.stockia.stockia.documentation.product;

import io.swagger.v3.oas.annotations.tags.Tag;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Tag principal para el controlador de productos en Swagger.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Tag(name = "04 - Productos", description = "API para gestión de productos del inventario")
public @interface ProductControllerTag {
}
