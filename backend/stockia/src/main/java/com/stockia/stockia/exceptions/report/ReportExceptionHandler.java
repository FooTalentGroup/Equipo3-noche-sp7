package com.stockia.stockia.exceptions.report;

import com.stockia.stockia.exceptions.ErrorResponse;
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
 * Manejador de excepciones específico para el módulo de reportes.
 * 
 * Tiene prioridad más alta que el GlobalExceptionHandler para proporcionar
 * mensajes de error más específicos en el contexto de reportes.
 */
@RestControllerAdvice(assignableTypes = {
                com.stockia.stockia.controllers.SalesReportController.class,
                com.stockia.stockia.controllers.ProductReportController.class
})
@Order(Ordered.HIGHEST_PRECEDENCE)
@Slf4j
public class ReportExceptionHandler {

        /**
         * Maneja IllegalArgumentException en el contexto de reportes.
         * 
         * Proporciona mensajes de error específicos para validaciones de negocio
         * como rangos de fechas inválidos, parámetros incorrectos, etc.
         */
        @ExceptionHandler(IllegalArgumentException.class)
        public ResponseEntity<ErrorResponse> handleIllegalArgumentException(
                        IllegalArgumentException ex, HttpServletRequest request) {

                log.warn("Argumento ilegal en reporte: {}", ex.getMessage());

                ErrorResponse errorResponse = new ErrorResponse(
                                HttpStatus.BAD_REQUEST.value(),
                                "BAD_REQUEST",
                                "Parámetros inválidos en el reporte",
                                Collections.singletonList(ex.getMessage()),
                                request.getRequestURI());

                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }

        /**
         * Maneja excepciones generales en el contexto de reportes.
         * 
         * Este handler captura cualquier excepción no manejada específicamente,
         * asegurando un mensaje de error consistente en el módulo de reportes.
         */
        @ExceptionHandler(Exception.class)
        public ResponseEntity<ErrorResponse> handleGlobalException(
                        Exception ex, HttpServletRequest request) {

                log.error("Error inesperado en módulo de reportes: {}", ex.getMessage(), ex);

                ErrorResponse errorResponse = new ErrorResponse(
                                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                "INTERNAL_SERVER_ERROR",
                                "Error interno al generar el reporte",
                                Collections.singletonList(
                                                "Se produjo un error inesperado. Por favor, intenta más tarde."),
                                request.getRequestURI());

                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
}
