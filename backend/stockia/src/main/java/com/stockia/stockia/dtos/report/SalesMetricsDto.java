package com.stockia.stockia.dtos.report;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

/**
 * DTO para las métricas principales del reporte de ventas.
 * Incluye ingreso total, cantidad de órdenes y ticket promedio,
 * junto con los porcentajes de cambio respecto al período anterior.
 */
@Schema(description = "Métricas principales del reporte de ventas")
public class SalesMetricsDto {

    @Schema(description = "Ingreso total por ventas en el período", example = "2500000.00")
    private BigDecimal totalRevenue;

    @Schema(description = "Cantidad total de ventas", example = "150")
    private Long totalOrders;

    @Schema(description = "Ticket promedio por venta", example = "45000.00")
    private BigDecimal averageTicket;

    @Schema(description = "Porcentaje de cambio en el ingreso total vs período anterior", example = "12.2")
    private Double revenueChangePercentage;

    @Schema(description = "Porcentaje de cambio en cantidad de ventas vs período anterior", example = "12.2")
    private Double ordersChangePercentage;

    @Schema(description = "Porcentaje de cambio en ticket promedio vs período anterior", example = "12.2")
    private Double averageTicketChangePercentage;

    public SalesMetricsDto() {
    }

    public SalesMetricsDto(BigDecimal totalRevenue, Long totalOrders, Double averageTicket) {
        this.totalRevenue = totalRevenue != null ? totalRevenue : BigDecimal.ZERO;
        this.totalOrders = totalOrders != null ? totalOrders : 0L;
        this.averageTicket = averageTicket != null ? BigDecimal.valueOf(averageTicket) : BigDecimal.ZERO;
    }

    public BigDecimal getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(BigDecimal totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    public Long getTotalOrders() {
        return totalOrders;
    }

    public void setTotalOrders(Long totalOrders) {
        this.totalOrders = totalOrders;
    }

    public BigDecimal getAverageTicket() {
        return averageTicket;
    }

    public void setAverageTicket(BigDecimal averageTicket) {
        this.averageTicket = averageTicket;
    }

    public Double getRevenueChangePercentage() {
        return revenueChangePercentage;
    }

    public void setRevenueChangePercentage(Double revenueChangePercentage) {
        this.revenueChangePercentage = revenueChangePercentage;
    }

    public Double getOrdersChangePercentage() {
        return ordersChangePercentage;
    }

    public void setOrdersChangePercentage(Double ordersChangePercentage) {
        this.ordersChangePercentage = ordersChangePercentage;
    }

    public Double getAverageTicketChangePercentage() {
        return averageTicketChangePercentage;
    }

    public void setAverageTicketChangePercentage(Double averageTicketChangePercentage) {
        this.averageTicketChangePercentage = averageTicketChangePercentage;
    }
}
