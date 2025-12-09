package com.stockia.stockia.services;

import com.stockia.stockia.dtos.report.SalesReportResponseDto;

import java.time.LocalDate;

/**
 * Servicio para la generación de reportes de ventas.
 * Proporciona métricas, análisis y visualizaciones de datos de ventas.
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
     * 
     * @param startDate   Fecha de inicio del período
     * @param endDate     Fecha de fin del período
     * @param productName Nombre del producto para filtrar (opcional, puede ser null
     *                    o vacío)
     * @return DTO con el reporte completo de ventas
     * @throws IllegalArgumentException si la fecha de fin es anterior a la fecha de
     *                                  inicio
     */
    SalesReportResponseDto generateSalesReport(LocalDate startDate, LocalDate endDate, String productName);
}
