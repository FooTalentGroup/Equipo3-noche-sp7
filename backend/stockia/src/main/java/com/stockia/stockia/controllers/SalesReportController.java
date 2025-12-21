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

@RestController
@RequestMapping("/api/reports/sales")
@RequiredArgsConstructor
@Slf4j
public class SalesReportController implements SalesReportSwaggerDoc {

        private final SalesReportService salesReportService;

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
