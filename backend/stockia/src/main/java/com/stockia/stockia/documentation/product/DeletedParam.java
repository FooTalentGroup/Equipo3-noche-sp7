package com.stockia.stockia.documentation.product;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Documentación para el parámetro 'deleted' en endpoints de productos.
 *
 * @author StockIA Team
 * @version 1.0
 * @since 2025-11-25
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Parameter(
        name = "deleted",
        description = "Filtrar solo productos eliminados (soft delete). " +
                "Si es true, retorna solo productos eliminados. " +
                "Si es false o no se especifica, retorna productos activos.",
        schema = @Schema(type = "boolean", defaultValue = "false"),
        example = "false"
)
public @interface DeletedParam {
}

