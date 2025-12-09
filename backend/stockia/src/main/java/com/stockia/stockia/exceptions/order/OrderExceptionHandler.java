package com.stockia.stockia.exceptions.order;

import com.stockia.stockia.exceptions.ErrorResponse;
import com.stockia.stockia.exceptions.product.InsufficientStockException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Collections;

/**
 * Manejador de excepciones específico para el módulo de órdenes de venta.
 * 
 * Tiene prioridad más alta que el GlobalExceptionHandler para proporcionar
 * mensajes de error más específicos en el contexto de órdenes.
 *
 * @author StockIA Team
 * @version 1.0
 * @since 2025-11-30
 */
@RestControllerAdvice(assignableTypes = {
        com.stockia.stockia.controllers.OrderController.class
})
@Order(Ordered.HIGHEST_PRECEDENCE)
@Slf4j
public class OrderExceptionHandler {

    /**
     * Maneja InsufficientStockException cuando no hay stock suficiente
     * para completar una orden de venta.
     * 
     * Retorna un error 400 BAD_REQUEST con detalles del producto y stock
     * disponible.
     */
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

    /**
     * Maneja InvalidOrderStatusException cuando se intenta realizar una operación
     * inválida debido al estado actual de la orden.
     * 
     * Ejemplos: confirmar una orden ya confirmada, cancelar una orden entregada,
     * etc.
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

    /**
     * Maneja OrderNotFoundException cuando no se encuentra una orden.
     * 
     * Retorna un error 404 NOT_FOUND con el ID o número de orden que no se
     * encontró.
     */
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

    /**
     * Maneja OrderCannotBeDeletedException cuando se intenta eliminar una orden
     * confirmada.
     * 
     * De acuerdo a RN-02, las órdenes confirmadas no pueden eliminarse,
     * solo pueden cancelarse para mantener el registro histórico.
     * 
     * Retorna un error 409 CONFLICT indicando que debe usarse la opción de
     * cancelar.
     */
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

    /**
     * Maneja IllegalArgumentException en el contexto de órdenes.
     * 
     * Proporciona mensajes de error específicos para validaciones de negocio
     * como productos no disponibles, precios inválidos, etc.
     */
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

    /**
     * Maneja excepciones generales en el contexto de órdenes.
     * 
     * Este handler captura cualquier excepción no manejada específicamente,
     * asegurando un mensaje de error consistente en el módulo de órdenes.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(
            Exception ex, HttpServletRequest request) {

        log.error("Error inesperado en módulo de órdenes: {}", ex.getMessage(), ex);

        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "INTERNAL_SERVER_ERROR",
                "Error interno al procesar la orden",
                Collections.singletonList("Se produjo un error inesperado. Por favor, intenta más tarde."),
                request.getRequestURI());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}
