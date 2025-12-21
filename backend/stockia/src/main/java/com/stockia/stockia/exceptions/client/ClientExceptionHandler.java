package com.stockia.stockia.exceptions.client;

import com.stockia.stockia.exceptions.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice(assignableTypes = {
        com.stockia.stockia.controllers.ClientController.class
})
@Order(Ordered.HIGHEST_PRECEDENCE)
@Slf4j
public class ClientExceptionHandler {

    @ExceptionHandler(ClientDuplicatedException.class)
    public ResponseEntity<ErrorResponse> handleClientDuplicatedException(
            ClientDuplicatedException ex, HttpServletRequest request) {

        log.warn("Cliente duplicado: {}", ex.getMessage());

        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.CONFLICT.value(),
                "CLIENT_DUPLICATED",
                "Cliente duplicado",
                Collections.singletonList(ex.getMessage()),
                request.getRequestURI());

        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    @ExceptionHandler(ClientNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleClientNotFoundException(
            ClientNotFoundException ex, HttpServletRequest request) {

        log.warn("Cliente no encontrado: {}", ex.getMessage());

        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                "CLIENT_NOT_FOUND",
                "Cliente no encontrado",
                Collections.singletonList(ex.getMessage()),
                request.getRequestURI());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(ClientProtectedException.class)
    public ResponseEntity<ErrorResponse> handleClientProtectedException(
            ClientProtectedException ex,
            HttpServletRequest request) {

        log.warn("Operación no permitida sobre cliente protegido: {}", ex.getMessage());

        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "CLIENT_PROTECTED",
                "Cliente protegido",
                Collections.singletonList(ex.getMessage()),
                request.getRequestURI()
        );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errorResponse);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(
            IllegalArgumentException ex, HttpServletRequest request) {

        log.warn("Argumento ilegal en cliente: {}", ex.getMessage());

        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "BAD_REQUEST",
                "Datos inválidos del cliente",
                Collections.singletonList(ex.getMessage()),
                request.getRequestURI());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex, HttpServletRequest request) {
        List<String> detalles = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(f -> f.getField() + ": " + f.getDefaultMessage())
                .collect(Collectors.toList());

        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "VALIDATION_ERROR",
                "Falló la validación de los campos",
                detalles,
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(
            Exception ex, HttpServletRequest request) {

        log.error("Error inesperado en módulo de clientes: {}", ex.getMessage(), ex);

        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "INTERNAL_SERVER_ERROR",
                "Error interno al procesar el cliente",
                Collections.singletonList("Se produjo un error inesperado. Por favor, intenta más tarde."),
                request.getRequestURI());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}
