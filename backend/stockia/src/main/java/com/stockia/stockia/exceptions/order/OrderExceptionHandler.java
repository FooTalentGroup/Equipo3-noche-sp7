package com.stockia.stockia.exceptions.order;

import com.stockia.stockia.exceptions.ErrorResponse;
import com.stockia.stockia.exceptions.client.ClientInactiveException;
import com.stockia.stockia.exceptions.client.ClientNotFoundException;
import com.stockia.stockia.exceptions.product.InsufficientStockException;
import com.stockia.stockia.exceptions.product.ProductNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Collections;
import java.util.List;

@RestControllerAdvice(assignableTypes = {
                com.stockia.stockia.controllers.OrderController.class
})
@Order(Ordered.HIGHEST_PRECEDENCE)
@Slf4j
public class OrderExceptionHandler {

        @ExceptionHandler(InsufficientStockException.class)
        public ResponseEntity<ErrorResponse> handleInsufficientStockException(
                        InsufficientStockException ex, HttpServletRequest request) {

                log.warn("Stock insuficiente en orden: {}", ex.getMessage());

                ErrorResponse errorResponse = new ErrorResponse(
                                HttpStatus.BAD_REQUEST.value(),
                                "INSUFFICIENT_STOCK",
                                "Stock insuficiente para completar la operación",
                                Collections.singletonList(ex.getMessage()),
                                request.getRequestURI());

                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }

        @ExceptionHandler(InvalidOrderStatusException.class)
        public ResponseEntity<ErrorResponse> handleInvalidOrderStatusException(
                        InvalidOrderStatusException ex, HttpServletRequest request) {

                log.warn("Estado de orden inválido: {}", ex.getMessage());
         * 
         * Retorna un error 400 BAD_REQUEST con detalles de la transición inválida.
         */
        @ExceptionHandler(InvalidOrderStatusException.class)
        public ResponseEntity<ErrorResponse> handleInvalidOrderStatusException(
                        InvalidOrderStatusException ex, HttpServletRequest request) {

                log.warn("Estado de orden inválido: {}", ex.getMessage());

                ErrorResponse errorResponse = new ErrorResponse(
                                HttpStatus.BAD_REQUEST.value(),
                                "INVALID_ORDER_STATUS",
                                "Operación no permitida para el estado actual de la orden",
                                Collections.singletonList(ex.getMessage()),
                                request.getRequestURI());

                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }

        @ExceptionHandler(OrderNotFoundException.class)
        public ResponseEntity<ErrorResponse> handleOrderNotFoundException(
                        OrderNotFoundException ex, HttpServletRequest request) {

                log.warn("Orden no encontrada: {}", ex.getMessage());

                ErrorResponse errorResponse = new ErrorResponse(
                                HttpStatus.NOT_FOUND.value(),
                                "ORDER_NOT_FOUND",
                                "Orden no encontrada",
                                Collections.singletonList(ex.getMessage()),
                                request.getRequestURI());

                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }

        @ExceptionHandler(OrderCannotBeDeletedException.class)
        public ResponseEntity<ErrorResponse> handleOrderCannotBeDeletedException(
                        OrderCannotBeDeletedException ex, HttpServletRequest request) {

                log.warn("Intento de eliminar orden confirmada: {}", ex.getMessage());

                ErrorResponse errorResponse = new ErrorResponse(
                                HttpStatus.CONFLICT.value(),
                                "ORDER_CANNOT_BE_DELETED",
                                "La orden no puede ser eliminada",
                                Collections.singletonList(ex.getMessage()),
                                request.getRequestURI());

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
                        request.getRequestURI());

                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
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

        @ExceptionHandler(ClientInactiveException.class)
        public ResponseEntity<ErrorResponse> handleClientInactiveException(
                ClientInactiveException ex,
                HttpServletRequest request) {

                log.warn("El cliente ingresado fue dado de baja: {}", ex.getMessage());

                ErrorResponse errorResponse = new ErrorResponse(
                        HttpStatus.BAD_REQUEST.value(),
                        "CLIENT_INACTIVE",
                        "Cliente inactivo",
                        Collections.singletonList(ex.getMessage()),
                        request.getRequestURI()
                );

                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(errorResponse);
        }

        @ExceptionHandler(HttpMessageNotReadableException.class)
        public ResponseEntity<ErrorResponse> handleInvalidFormat(HttpMessageNotReadableException ex,
                                                                 HttpServletRequest request) {

                Throwable cause = ex.getCause();

                if (cause instanceof com.fasterxml.jackson.databind.exc.InvalidFormatException ife) {
                        if (ife.getTargetType() == java.util.UUID.class) {

                                String valorInvalido = String.valueOf(ife.getValue());

                                ErrorResponse error = new ErrorResponse(
                                        HttpStatus.BAD_REQUEST.value(),
                                        "INVALID_UUID_FORMAT",
                                        "Formato inválido para un campo UUID",
                                        List.of("Valor recibido: " + valorInvalido),
                                        request.getRequestURI()
                                );

                                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
                        }
                }

                ErrorResponse error = new ErrorResponse(
                        HttpStatus.BAD_REQUEST.value(),
                        "MALFORMED_JSON",
                        "El cuerpo de la solicitud no es válido",
                        List.of(ex.getMessage()),
                        request.getRequestURI()
                );

                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }

        @ExceptionHandler(IllegalArgumentException.class)
        public ResponseEntity<ErrorResponse> handleIllegalArgumentException(
                        IllegalArgumentException ex, HttpServletRequest request) {

                log.warn("Argumento ilegal en orden: {}", ex.getMessage());

                ErrorResponse errorResponse = new ErrorResponse(
                                HttpStatus.BAD_REQUEST.value(),
                                "BAD_REQUEST",
                                "Datos inválidos en la orden",
                                Collections.singletonList(ex.getMessage()),
                                request.getRequestURI());

                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }

        @ExceptionHandler(Exception.class)
        public ResponseEntity<ErrorResponse> handleGlobalException(
                        Exception ex, HttpServletRequest request) {

                log.error("Error inesperado en módulo de órdenes: {}", ex.getMessage(), ex);

                ErrorResponse errorResponse = new ErrorResponse(
                                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                "INTERNAL_SERVER_ERROR",
                                "Error interno al procesar la orden",
                                Collections.singletonList(
                                                "Se produjo un error inesperado. Por favor, intenta más tarde."),
                                request.getRequestURI());

                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
}
