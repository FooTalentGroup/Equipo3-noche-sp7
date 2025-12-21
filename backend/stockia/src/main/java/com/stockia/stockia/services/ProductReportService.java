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

    Page<MostSoldProductDto> getMostSoldProducts(LocalDate startDate, LocalDate endDate, Pageable pageable);

    List<MonthlyCostDto> getCostReport(Integer year, String categoryName, String productName);

    List<DailyStockDto> getStockReport(String productName, LocalDate startDate, LocalDate endDate);
}
