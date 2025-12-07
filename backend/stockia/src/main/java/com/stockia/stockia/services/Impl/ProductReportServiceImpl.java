package com.stockia.stockia.services.Impl;

import com.stockia.stockia.dtos.report.MostSoldProductDto;
import com.stockia.stockia.repositories.OrderItemRepository;
import com.stockia.stockia.services.ProductReportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ProductReportServiceImpl implements ProductReportService {

    private final OrderItemRepository orderItemRepository;

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
}
