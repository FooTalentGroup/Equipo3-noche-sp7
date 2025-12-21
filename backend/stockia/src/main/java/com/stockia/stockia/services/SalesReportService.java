package com.stockia.stockia.services;

import com.stockia.stockia.dtos.report.SalesReportResponseDto;

import java.time.LocalDate;

/**
 * Servicio para la generación de reportes de ventas.
 */
public interface SalesReportService {

    /**
     * Genera un reporte completo de ventas para el período especificado.
     * 
     * Incluye:
     * - Métricas principales (ingreso total, cantidad de ventas, ticket promedio)
     * - Comparación con el período anterior
     * - Ventas diarias para gráficos
     * - Distribución por método de pago
     */
    SalesReportResponseDto generateSalesReport(LocalDate startDate, LocalDate endDate, String productName);
}
