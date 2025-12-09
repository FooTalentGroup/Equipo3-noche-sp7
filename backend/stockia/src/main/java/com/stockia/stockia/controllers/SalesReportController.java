package com.stockia.stockia.controllers;

import com.stockia.stockia.documentation.report.SalesReportSwaggerDoc;
import com.stockia.stockia.dtos.report.SalesReportResponseDto;
import com.stockia.stockia.services.SalesReportService;
import com.stockia.stockia.utils.ApiResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

/**
 * Controlador REST para reportes de ventas.
 * Proporciona endpoints para generar y consultar análisis de ventas.
 * Requiere autenticación y rol ADMIN.
 */
@RestController
@RequestMapping("/api/reports/sales")
@RequiredArgsConstructor
@Slf4j
public class SalesReportController implements SalesReportSwaggerDoc {

        private final SalesReportService salesReportService;

        /**
         * Genera el reporte completo de ventas para un período específico.
         *
         * @param startDate   Fecha de inicio del reporte (formato: yyyy-MM-dd)
         * @param endDate     Fecha de fin del reporte (formato: yyyy-MM-dd)
         * @param productName Nombre del producto para filtrar (opcional)
         * @return ResponseEntity con el reporte generado
         */
        @GetMapping
        @PreAuthorize("hasRole('ADMIN')")
        @Override
        public ResponseEntity<ApiResult<SalesReportResponseDto>> generateSalesReport(
                        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
                        @RequestParam(required = false) String productName) {

                log.info("Solicitud de reporte de ventas - Inicio: {}, Fin: {}, Producto: {}",
                                startDate, endDate, productName);

                SalesReportResponseDto report = salesReportService.generateSalesReport(
                                startDate, endDate, productName);

                return ResponseEntity.ok(
                                ApiResult.success("Reporte generado exitosamente", report));
        }
}
