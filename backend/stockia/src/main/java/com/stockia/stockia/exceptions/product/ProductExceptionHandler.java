package com.stockia.stockia.exceptions.product;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.stockia.stockia.dtos.ErrorResponseData;
import com.stockia.stockia.exceptions.category.CategoryNotFoundException;
import com.stockia.stockia.exceptions.category.DuplicateCategoryException;
import com.stockia.stockia.utils.ApiResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class ProductExceptionHandler {

    /**
     * Maneja errores de validación de campos anotados con @Valid.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResult<ErrorResponseData>> handleValidationExceptions(
            MethodArgumentNotValidException ex, WebRequest request) {

        log.warn("Validation error: {}", ex.getMessage());
        Map<String, String> fieldErrors = new HashMap<>();
        List<String> errorMessages = new ArrayList<>();

        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            fieldErrors.put(fieldName, errorMessage);
            errorMessages.add(errorMessage);
        });

        ErrorResponseData errorData = ErrorResponseData.builder()
                .fields(fieldErrors)
                .errors(errorMessages)
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResult.error("Errores de validación", errorData));
    }

    /**
     * Maneja errores de formato JSON y tipos de datos incorrectos.
     * Ejemplo: enviar string donde se espera número, JSON malformado, etc.
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResult<ErrorResponseData>> handleHttpMessageNotReadableException(
            HttpMessageNotReadableException ex, WebRequest request) {

        log.warn("Request body error: {}", ex.getMessage());
        Map<String, String> fieldErrors = new HashMap<>();
        List<String> errorMessages = new ArrayList<>();
        String mainMessage = "Error en el formato de los datos enviados";

        // Verificar si es un error de tipo de dato incorrecto
        Throwable cause = ex.getCause();
        if (cause instanceof InvalidFormatException) {
            InvalidFormatException ife = (InvalidFormatException) cause;
            String fieldName = ife.getPath().isEmpty() ? "campo desconocido"
                    : ife.getPath().get(0).getFieldName();
            String targetType = ife.getTargetType().getSimpleName();
            String value = ife.getValue() != null ? ife.getValue().toString() : "null";

            mainMessage = String.format(
                "El campo '%s' tiene un formato inválido. Se esperaba un tipo '%s' pero se recibió '%s'",
                fieldName, getSpanishTypeName(targetType), value
            );
            fieldErrors.put(fieldName, mainMessage);
            errorMessages.add(mainMessage);

        } else if (cause instanceof MismatchedInputException) {
            MismatchedInputException mie = (MismatchedInputException) cause;
            String fieldName = mie.getPath().isEmpty() ? "campo desconocido"
                    : mie.getPath().get(0).getFieldName();
            String targetType = mie.getTargetType().getSimpleName();

            mainMessage = String.format(
                "El campo '%s' es requerido o tiene un formato incorrecto. Se esperaba un tipo '%s'",
                fieldName, getSpanishTypeName(targetType)
            );
            fieldErrors.put(fieldName, mainMessage);
            errorMessages.add(mainMessage);

        } else {
            // JSON malformado o error genérico
            mainMessage = "El formato del JSON es inválido. Verifica la estructura de los datos";
            fieldErrors.put("json", mainMessage);
            errorMessages.add(mainMessage);
        }

        ErrorResponseData errorData = ErrorResponseData.builder()
                .fields(fieldErrors)
                .errors(errorMessages)
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResult.error(mainMessage, errorData));
    }

    /**
     * Maneja errores de tipos de parámetros incorrectos en URL o query params.
     * Ejemplo: enviar "abc" donde se espera un número en /api/products/{id}
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResult<ErrorResponseData>> handleMethodArgumentTypeMismatchException(
            MethodArgumentTypeMismatchException ex, WebRequest request) {

        log.warn("Parameter type mismatch: {}", ex.getMessage());
        Map<String, String> fieldErrors = new HashMap<>();
        List<String> errorMessages = new ArrayList<>();

        String paramName = ex.getName();
        String requiredType = ex.getRequiredType() != null
                ? ex.getRequiredType().getSimpleName()
                : "desconocido";
        String value = ex.getValue() != null ? ex.getValue().toString() : "null";

        String errorMessage = String.format(
            "El parámetro '%s' tiene un formato inválido. Se esperaba un '%s' pero se recibió '%s'",
            paramName, getSpanishTypeName(requiredType), value
        );

        fieldErrors.put(paramName, errorMessage);
        errorMessages.add(errorMessage);

        ErrorResponseData errorData = ErrorResponseData.builder()
                .fields(fieldErrors)
                .errors(errorMessages)
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResult.error(errorMessage, errorData));
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
    public ResponseEntity<ApiResult<Void>> handleDuplicateProductException(
            DuplicateProductException ex, WebRequest request) {
        log.warn("Duplicate product error: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ApiResult.error(ex.getMessage()));
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ApiResult<Void>> handleProductNotFoundException(
            ProductNotFoundException ex, WebRequest request) {
        log.warn("Product not found: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResult.error(ex.getMessage()));
    }

    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<ApiResult<Void>> handleCategoryNotFoundException(
            CategoryNotFoundException ex, WebRequest request) {
        log.warn("Category not found: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResult.error(ex.getMessage()));
    }

    @ExceptionHandler(DuplicateCategoryException.class)
    public ResponseEntity<ApiResult<Void>> handleDuplicateCategoryException(
            DuplicateCategoryException ex, WebRequest request) {
        log.warn("Duplicate category error: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ApiResult.error(ex.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResult<Void>> handleIllegalArgumentException(
            IllegalArgumentException ex, WebRequest request) {
        log.warn("Illegal argument: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResult.error(ex.getMessage()));
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ApiResult<Void>> handleIllegalStateException(
            IllegalStateException ex, WebRequest request) {
        log.warn("Illegal state: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ApiResult.error(ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResult<Void>> handleGlobalException(
            Exception ex, WebRequest request) {
        log.error("Unexpected error occurred: {}", ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResult.error("Error interno del servidor. Por favor contacte al administrador."));
    }
}

