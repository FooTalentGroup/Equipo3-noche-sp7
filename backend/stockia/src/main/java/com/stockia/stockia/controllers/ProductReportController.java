package com.stockia.stockia.controllers;

import com.stockia.stockia.documentation.product.ProductIdParam;
import com.stockia.stockia.documentation.report.ProductReportSwaggerDoc.*;
import com.stockia.stockia.dtos.report.DailyStockDto;
import com.stockia.stockia.dtos.report.MonthlyCostDto;
import com.stockia.stockia.dtos.report.MostSoldProductDto;
import com.stockia.stockia.services.ProductReportService;
import com.stockia.stockia.utils.ApiResult;
import jakarta.validation.constraints.NotBlank;
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
 * - GET [/api/reports/products/most-sold] → Productos más vendidos
 * - GET [/api/reports/products/costs] → Reporte de costos mensuales
 * - GET [/api/reports/products/stock] → Reporte de stock diario
 */
@ProductReportControllerTag
@RestController
@RequestMapping("/api/reports/products")
@RequiredArgsConstructor
@Slf4j
public class ProductReportController {

        private final ProductReportService productReportService;

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

        @GetMapping("/costs")
        @GetCostReportDoc
        @PreAuthorize("hasRole('ADMIN')")
        public ResponseEntity<ApiResult<List<MonthlyCostDto>>> getCostReport(
                        @YearParam @RequestParam @NotNull(message = "El año es obligatorio") Integer year,

                        @CategoryNameParam @RequestParam(required = false) String categoryName,

                        @ProductNameParam @RequestParam(required = false) String productName) {

                log.info("GET /api/reports/products/costs - year: {}, categoryName: {}, productName: {}",
                                year, categoryName, productName);

                List<MonthlyCostDto> report = productReportService.getCostReport(year, categoryName, productName);

                log.info("Successfully generated cost report with 12 months for year {}", year);

                return ResponseEntity.ok(
                                ApiResult.success("Reporte de costos generado exitosamente", report));
        }

        @GetMapping("/stock")
        @GetStockReportDoc
        @PreAuthorize("hasRole('ADMIN')")
        public ResponseEntity<ApiResult<List<DailyStockDto>>> getStockReport(
                        @ProductNameParam @RequestParam @NotBlank(message = "El nombre del producto es obligatorio") String productName,

                        @StartDateParam @RequestParam @NotNull(message = "La fecha de inicio es obligatoria") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,

                        @EndDateParam @RequestParam @NotNull(message = "La fecha de fin es obligatoria") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

                log.info("GET /api/reports/products/stock - productName: {}, startDate: {}, endDate: {}",
                                productName, startDate, endDate);

                if (endDate.isBefore(startDate)) {
                        log.warn("Invalid date range: endDate {} is before startDate {}", endDate, startDate);
                        return ResponseEntity.badRequest()
                                        .body(ApiResult.error(
                                                        "La fecha de fin debe ser mayor o igual a la fecha de inicio"));
                }

                List<DailyStockDto> report = productReportService.getStockReport(productName, startDate, endDate);

                log.info("Successfully generated stock report with {} days", report.size());

                return ResponseEntity.ok(
                                ApiResult.success("Reporte de stock generado exitosamente", report));
        }
}
