package com.stockia.stockia.dtos;

public record HomeStatsResponseDto(
        long lowStockProducts,
        long activeProducts,
        long weeklySales,
        long activeClients
) {}
