package com.stockia.stockia.controllers;

import com.stockia.stockia.documentation.report.ProductReportSwaggerDoc.*;
import com.stockia.stockia.dtos.report.MonthlyCostDto;
import com.stockia.stockia.dtos.report.MostSoldProductDto;
import com.stockia.stockia.services.ProductReportService;
import com.stockia.stockia.utils.ApiResult;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/**
 * Controlador REST para generación de reportes de productos.
 * 
 * Proporciona endpoints para obtener estadísticas y reportes
 * basados en las ventas y el inventario de productos.
 *
 * Endpoints disponibles:
 * - GET [/api/reports/products/most-sold] → Productos más vendidos en un
 * periodo
 * - GET [/api/reports/products/costs] → Reporte de costos mensuales
 */
@ProductReportControllerTag
@RestController
@RequestMapping("/api/reports/products")
@RequiredArgsConstructor
@Slf4j
public class ProductReportController {

        private final ProductReportService productReportService;

        /**
         * Obtiene un reporte de los productos más vendidos en un periodo específico.
         *
         * @param startDate Fecha de inicio del periodo (formato: yyyy-MM-dd)
         * @param endDate   Fecha de fin del periodo (formato: yyyy-MM-dd)
         * @param pageable  Configuración de paginación
         * @return Respuesta con página de productos más vendidos
         */
        @GetMapping("/most-sold")
        @GetMostSoldProductsDoc
        @PreAuthorize("hasRole('ADMIN')")
        public ResponseEntity<ApiResult<Page<MostSoldProductDto>>> getMostSoldProducts(
                        @StartDateParam @RequestParam @NotNull(message = "La fecha de inicio es obligatoria") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,

                        @EndDateParam @RequestParam @NotNull(message = "La fecha de fin es obligatoria") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,

                        @ParameterObject Pageable pageable) {

                log.info("GET /api/reports/products/most-sold - startDate: {}, endDate: {}, page: {}, size: {}",
                                startDate, endDate, pageable.getPageNumber(), pageable.getPageSize());

                if (endDate.isBefore(startDate)) {
                        log.warn("Invalid date range: endDate {} is before startDate {}", endDate, startDate);
                        return ResponseEntity.badRequest()
                                        .body(ApiResult.error(
                                                        "La fecha de fin debe ser mayor o igual a la fecha de inicio"));
                }

                Page<MostSoldProductDto> report = productReportService.getMostSoldProducts(startDate, endDate,
                                pageable);

                log.info("Successfully generated report with {} products", report.getTotalElements());

                return ResponseEntity.ok(
                                ApiResult.success("Reporte generado exitosamente", report));
        }

        /**
         * Obtiene el reporte de costos mensuales de productos.
         *
         * @param year       Año a consultar (requerido)
         * @param categoryId ID de categoría para filtrar (opcional)
         * @param productId  ID de producto específico para filtrar (opcional)
         * @return Respuesta con datos mensuales de costos
         */
        @GetMapping("/costs")
        @GetCostReportDoc
        @PreAuthorize("hasRole('ADMIN')")
        public ResponseEntity<ApiResult<List<MonthlyCostDto>>> getCostReport(
                        @YearParam @RequestParam @NotNull(message = "El año es obligatorio") Integer year,

                        @CategoryIdParam @RequestParam(required = false) UUID categoryId,

                        @ProductIdParam @RequestParam(required = false) UUID productId) {

                log.info("GET /api/reports/products/costs - year: {}, categoryId: {}, productId: {}",
                                year, categoryId, productId);

                List<MonthlyCostDto> report = productReportService.getCostReport(year, categoryId, productId);

                log.info("Successfully generated cost report with 12 months for year {}", year);

                return ResponseEntity.ok(
                                ApiResult.success("Reporte de costos generado exitosamente", report));
        }
}
