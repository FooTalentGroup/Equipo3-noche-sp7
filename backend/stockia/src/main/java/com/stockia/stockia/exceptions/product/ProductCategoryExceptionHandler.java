package com.stockia.stockia.exceptions.product;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.stockia.stockia.exceptions.ErrorResponse;
import com.stockia.stockia.exceptions.category.CategoryNotFoundException;
import com.stockia.stockia.exceptions.category.DuplicateCategoryException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice(assignableTypes = {
    com.stockia.stockia.controllers.ProductController.class,
    com.stockia.stockia.controllers.CategoryController.class
})
@Order(Ordered.HIGHEST_PRECEDENCE)
@Slf4j
public class ProductCategoryExceptionHandler {

    /**
     * Maneja errores de validación de campos anotados con @Valid.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(
            MethodArgumentNotValidException ex, HttpServletRequest request) {

        log.warn("Validation error in products/categories: {}", ex.getMessage());

        List<String> details = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(f -> f.getField() + ": " + f.getDefaultMessage())
                .collect(Collectors.toList());

        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "VALIDATION_ERROR",
                "Falló la validación de los campos",
                details,
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    /**
     * Maneja errores de formato JSON y tipos de datos incorrectos.
     * Intenta detectar TODOS los errores de formato en el JSON para reportarlos juntos.
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(
            HttpMessageNotReadableException ex, HttpServletRequest request) {

        log.warn("Request body error in products/categories: {}", ex.getMessage());

        List<String> details = new ArrayList<>();
        String message = "Errores de formato en los datos";

        Throwable cause = ex.getCause();

        // Intentar extraer el JSON raw del request para validar todos los campos
        try {
            String jsonBody = extractJsonFromRequest(request);
            if (jsonBody != null && !jsonBody.isEmpty()) {
                details = validateAllJsonFields(jsonBody);
            }
        } catch (Exception e) {
            log.debug("No se pudo extraer el JSON raw, usando detección estándar");
        }

        // Si no se pudieron detectar múltiples errores, usar el error principal
        if (details.isEmpty()) {
            if (cause instanceof InvalidFormatException) {
                InvalidFormatException ife = (InvalidFormatException) cause;
                String fieldName = ife.getPath().isEmpty() ? "campo desconocido"
                        : ife.getPath().get(0).getFieldName();
                String targetType = ife.getTargetType().getSimpleName();
                String value = ife.getValue() != null ? ife.getValue().toString() : "null";

                if ("UUID".equals(targetType)) {
                    details.add(String.format("%s: Debe ser un UUID válido, se recibió '%s'", fieldName, value));
                } else {
                    details.add(String.format("%s: Se esperaba %s, se recibió '%s'",
                        fieldName, getSpanishTypeName(targetType), value));
                }

            } else if (cause instanceof MismatchedInputException) {
                MismatchedInputException mie = (MismatchedInputException) cause;
                String fieldName = mie.getPath().isEmpty() ? "campo desconocido"
                        : mie.getPath().get(0).getFieldName();
                String targetType = mie.getTargetType().getSimpleName();

                details.add(String.format("%s: Campo requerido o formato incorrecto (tipo esperado: %s)",
                    fieldName, getSpanishTypeName(targetType)));

            } else {
                message = "JSON mal formado";
                details.add("El JSON tiene errores de sintaxis. Verifica comillas, llaves y comas");
            }
        }

        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "INVALID_REQUEST_BODY",
                message,
                details,
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    /**
     * Extrae el JSON raw del HttpServletRequest.
     */
    private String extractJsonFromRequest(HttpServletRequest request) {
        try {
            return request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Valida todos los campos del JSON y retorna una lista de errores encontrados.
     */
    private List<String> validateAllJsonFields(String jsonBody) {
        List<String> errors = new ArrayList<>();

        try {
            // Parsear el JSON manualmente para extraer valores
            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            com.fasterxml.jackson.databind.JsonNode jsonNode = mapper.readTree(jsonBody);

            // Validar categoryId si está presente
            if (jsonNode.has("categoryId") && !jsonNode.get("categoryId").isNull()) {
                String categoryIdValue = jsonNode.get("categoryId").asText();
                if (!isValidUUID(categoryIdValue)) {
                    errors.add(String.format("categoryId: Debe ser un UUID válido, se recibió '%s'", categoryIdValue));
                }
            }

            // Validar productId si está presente
            if (jsonNode.has("productId") && !jsonNode.get("productId").isNull()) {
                String productIdValue = jsonNode.get("productId").asText();
                if (!isValidUUID(productIdValue)) {
                    errors.add(String.format("productId: Debe ser un UUID válido, se recibió '%s'", productIdValue));
                }
            }

            // Validar price si está presente
            if (jsonNode.has("price") && !jsonNode.get("price").isNull()) {
                String priceValue = jsonNode.get("price").asText();
                if (!isValidDecimal(priceValue)) {
                    errors.add(String.format("price: Se esperaba número decimal, se recibió '%s'", priceValue));
                }
            }

            // Validar minStock si está presente
            if (jsonNode.has("minStock") && !jsonNode.get("minStock").isNull()) {
                String minStockValue = jsonNode.get("minStock").asText();
                if (!isValidInteger(minStockValue)) {
                    errors.add(String.format("minStock: Se esperaba número entero, se recibió '%s'", minStockValue));
                }
            }

            // Validar currentStock si está presente
            if (jsonNode.has("currentStock") && !jsonNode.get("currentStock").isNull()) {
                String currentStockValue = jsonNode.get("currentStock").asText();
                if (!isValidInteger(currentStockValue)) {
                    errors.add(String.format("currentStock: Se esperaba número entero, se recibió '%s'", currentStockValue));
                }
            }

            // Validar isAvailable si está presente
            if (jsonNode.has("isAvailable") && !jsonNode.get("isAvailable").isNull()) {
                com.fasterxml.jackson.databind.JsonNode isAvailableNode = jsonNode.get("isAvailable");
                if (!isAvailableNode.isBoolean()) {
                    String value = isAvailableNode.asText();
                    errors.add(String.format("isAvailable: Se esperaba booleano (true/false), se recibió '%s'", value));
                }
            }

        } catch (Exception e) {
            log.debug("Error al parsear JSON para validación múltiple: {}", e.getMessage());
        }

        return errors;
    }

    /**
     * Valida si un string es un UUID válido.
     */
    private boolean isValidUUID(String value) {
        try {
            java.util.UUID.fromString(value);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Valida si un string es un número decimal válido.
     */
    private boolean isValidDecimal(String value) {
        try {
            new java.math.BigDecimal(value);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Valida si un string es un número entero válido.
     */
    private boolean isValidInteger(String value) {
        try {
            Integer.parseInt(value);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Maneja errores de tipos de parámetros incorrectos en URL o query params.
     * Ejemplo: enviar "abc" donde se espera un UUID en /api/products/{id}
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatchException(
            MethodArgumentTypeMismatchException ex, HttpServletRequest request) {

        log.warn("Parameter type mismatch in products/categories: {}", ex.getMessage());

        String paramName = ex.getName();
        String requiredType = ex.getRequiredType() != null
                ? ex.getRequiredType().getSimpleName()
                : "desconocido";
        String value = ex.getValue() != null ? ex.getValue().toString() : "null";

        String message;
        List<String> details = new ArrayList<>();

        // Detectar si es un error de UUID
        if (ex.getRequiredType() != null && ex.getRequiredType().equals(java.util.UUID.class)) {
            message = "ID con formato inválido";
            details.add(String.format("El parámetro '%s' debe ser un UUID válido", paramName));
            details.add("Formato esperado: '123e4567-e89b-12d3-a456-426614174000'");
        } else {
            message = "Parámetro con formato inválido";
            details.add(String.format("El parámetro '%s' tiene un tipo de dato incorrecto", paramName));
            details.add(String.format("Tipo esperado: %s", getSpanishTypeName(requiredType)));
        }

        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "INVALID_PARAMETER",
                message,
                details,
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    /**
     * Convierte nombres de tipos Java a términos más amigables en español.
     */
    private String getSpanishTypeName(String javaType) {
        return switch (javaType) {
            case "Long", "long" -> "número entero";
            case "Integer", "int" -> "número entero";
            case "BigDecimal" -> "número decimal";
            case "Double", "double" -> "número decimal";
            case "Float", "float" -> "número decimal";
            case "String" -> "texto";
            case "Boolean", "boolean" -> "booleano (true/false)";
            case "LocalDateTime" -> "fecha y hora";
            case "LocalDate" -> "fecha";
            default -> javaType;
        };
    }

    @ExceptionHandler(DuplicateProductException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateProductException(
            DuplicateProductException ex, HttpServletRequest request) {
        log.warn("Duplicate product error: {}", ex.getMessage());

        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.CONFLICT.value(),
                "CONFLICT",
                ex.getMessage(),
                Collections.singletonList("El nombre del producto debe ser único"),
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleProductNotFoundException(
            ProductNotFoundException ex, HttpServletRequest request) {
        log.warn("Product not found: {}", ex.getMessage());

        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                "NOT_FOUND",
                ex.getMessage(),
                Collections.singletonList("El producto especificado no existe en el sistema"),
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleCategoryNotFoundException(
            CategoryNotFoundException ex, HttpServletRequest request) {
        log.warn("Category not found: {}", ex.getMessage());

        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                "NOT_FOUND",
                ex.getMessage(),
                Collections.singletonList("La categoría especificada no existe en el sistema"),
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(DuplicateCategoryException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateCategoryException(
            DuplicateCategoryException ex, HttpServletRequest request) {
        log.warn("Duplicate category error: {}", ex.getMessage());

        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.CONFLICT.value(),
                "CONFLICT",
                ex.getMessage(),
                Collections.singletonList("El nombre de la categoría debe ser único"),
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(
            IllegalArgumentException ex, HttpServletRequest request) {
        log.warn("Illegal argument in products/categories: {}", ex.getMessage());

        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "BAD_REQUEST",
                "Solicitud inválida",
                Collections.singletonList(ex.getMessage()),
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorResponse> handleIllegalStateException(
            IllegalStateException ex, HttpServletRequest request) {
        log.warn("Illegal state in products/categories: {}", ex.getMessage());

        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.CONFLICT.value(),
                "CONFLICT",
                ex.getMessage(),
                Collections.singletonList("La operación no puede completarse en el estado actual del recurso"),
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(
            Exception ex, HttpServletRequest request) {
        log.error("Unexpected error in products/categories: {}", ex.getMessage(), ex);

        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "INTERNAL_SERVER_ERROR",
                "Error interno del servidor",
                Collections.singletonList("Se produjo un error inesperado. Por favor, contacte al administrador."),
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}

