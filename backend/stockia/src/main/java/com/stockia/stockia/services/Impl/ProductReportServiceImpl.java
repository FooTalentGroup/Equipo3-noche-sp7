package com.stockia.stockia.services.Impl;

import com.stockia.stockia.dtos.report.MonthlyCostDto;
import com.stockia.stockia.dtos.report.MostSoldProductDto;
import com.stockia.stockia.repositories.InventoryMovementRepository;
import com.stockia.stockia.repositories.OrderItemRepository;
import com.stockia.stockia.services.ProductReportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ProductReportServiceImpl implements ProductReportService {

    private final OrderItemRepository orderItemRepository;
    private final InventoryMovementRepository inventoryMovementRepository;

    @Override
    public Page<MostSoldProductDto> getMostSoldProducts(LocalDate startDate, LocalDate endDate, Pageable pageable) {
        log.info("Generating most sold products report for period: {} to {}", startDate, endDate);

        // Convertir LocalDate a LocalDateTime
        // startDate se convierte al inicio del día (00:00:00)
        // endDate se convierte al final del día (23:59:59.999999999)
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);

        log.debug("Querying most sold products between {} and {}", startDateTime, endDateTime);

        Page<MostSoldProductDto> result = orderItemRepository.findMostSoldProducts(
                startDateTime,
                endDateTime,
                pageable);

        log.info("Found {} products in report, total elements: {}, page: {}/{}",
                result.getNumberOfElements(),
                result.getTotalElements(),
                result.getNumber() + 1,
                result.getTotalPages());

        return result;
    }

    @Override
    public List<MonthlyCostDto> getCostReport(Integer year, UUID categoryId, UUID productId) {
        log.info("Generating cost report for year: {}, categoryId: {}, productId: {}",
                year, categoryId, productId);

        List<MonthlyCostDto> salesData = orderItemRepository.findMonthlySalesData(year, categoryId, productId);

        Map<Integer, MonthlyCostDto> salesByMonth = salesData.stream()
                .collect(Collectors.toMap(MonthlyCostDto::month, dto -> dto));

        List<MonthlyCostDto> result = new ArrayList<>();
        BigDecimal previousTotalCost = null;

        for (int month = 1; month <= 12; month++) {
            MonthlyCostDto monthlyData = salesByMonth.getOrDefault(month, MonthlyCostDto.empty(month));

            BigDecimal avgCost = inventoryMovementRepository.findAverageCostByMonth(
                    productId, categoryId, year, month);

            avgCost = avgCost != null ? avgCost : BigDecimal.ZERO;

            BigDecimal totalCost = avgCost.multiply(BigDecimal.valueOf(monthlyData.unitsSold()))
                    .setScale(2, RoundingMode.HALF_UP);

            BigDecimal variation = BigDecimal.ZERO;
            if (previousTotalCost != null && previousTotalCost.compareTo(BigDecimal.ZERO) > 0) {
                variation = totalCost.subtract(previousTotalCost)
                        .divide(previousTotalCost, 4, RoundingMode.HALF_UP)
                        .multiply(BigDecimal.valueOf(100))
                        .setScale(1, RoundingMode.HALF_UP);
            }

            MonthlyCostDto completeData = monthlyData.withCostData(avgCost, totalCost, variation);
            result.add(completeData);

            if (monthlyData.unitsSold() > 0) {
                previousTotalCost = totalCost;
            }
        }

        log.info("Generated cost report with 12 months for year {}", year);
        return result;
    }
}
