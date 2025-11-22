package com.stockia.stockia.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * DTO para respuestas de error con información detallada.
 * Incluye un mapa de errores por campo y una lista de mensajes para facilitar el manejo en frontend.
 *
 * @author StockIA Team
 * @version 1.0
 * @since 2025-11-21
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponseData {

    /**
     * Mapa de errores por campo.
     * Key: nombre del campo con error
     * Value: mensaje de error específico del campo
     */
    private Map<String, String> fields;

    /**
     * Lista de mensajes de error para facilitar el manejo en frontend.
     * Contiene todos los mensajes de error de forma plana.
     */
    @Builder.Default
    private List<String> errors = new ArrayList<>();
}

