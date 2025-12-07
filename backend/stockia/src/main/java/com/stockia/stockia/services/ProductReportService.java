package com.stockia.stockia.services;

import com.stockia.stockia.dtos.report.MonthlyCostDto;
import com.stockia.stockia.dtos.report.MostSoldProductDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface ProductReportService {

    /**
     * Obtiene un reporte de los productos más vendidos en un periodo específico.
     * 
     * Los resultados incluyen:
     * - Información del producto (nombre, categoría, precio)
     * - Cantidad inicial al inicio del periodo
     * - Cantidad vendida durante el periodo
     * - Cantidad actual en stock
     * 
     * Solo se consideran órdenes con estado CONFIRMED o DELIVERED.
     * Los resultados están ordenados por cantidad vendida descendente.
     * 
     * @param startDate Fecha de inicio del periodo (inclusive)
     * @param endDate   Fecha de fin del periodo (inclusive)
     * @param pageable  Configuración de paginación y ordenamiento
     * @return Página con los productos más vendidos y sus estadísticas
     */
    Page<MostSoldProductDto> getMostSoldProducts(LocalDate startDate, LocalDate endDate, Pageable pageable);

    /**
     * Obtiene el reporte de costos mensuales de productos.
     * Retorna datos agregados por mes para un año específico.
     * 
     * @param year       Año a consultar
     * @param categoryId ID de categoría (opcional)
     * @param productId  ID de producto (opcional)
     * @return Lista de datos mensuales con costos y ventas
     */
    List<MonthlyCostDto> getCostReport(Integer year, UUID categoryId, UUID productId);
}
