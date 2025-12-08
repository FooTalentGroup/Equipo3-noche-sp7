package com.stockia.stockia.services;

import com.stockia.stockia.dtos.report.DailyStockDto;
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
     * @param year         Año a consultar
     * @param categoryName Nombre de categoría (opcional)
     * @param productName  Nombre de producto (opcional)
     * @return Lista de datos mensuales con costos y ventas
     */
    List<MonthlyCostDto> getCostReport(Integer year, String categoryName, String productName);

    /**
     * Obtiene el reporte de stock diario de un producto específico.
     * Retorna evolución del stock día a día en un período determinado.
     * 
     * @param productName Nombre del producto a consultar
     * @param startDate   Fecha de inicio del período
     * @param endDate     Fecha de fin del período
     * @return Lista de datos diarios con movimientos de stock
     */
    List<DailyStockDto> getStockReport(String productName, LocalDate startDate, LocalDate endDate);
}
