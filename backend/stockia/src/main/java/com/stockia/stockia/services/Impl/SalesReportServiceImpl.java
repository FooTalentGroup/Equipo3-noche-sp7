package com.stockia.stockia.services.Impl;

import com.stockia.stockia.dtos.report.*;
import com.stockia.stockia.repositories.OrderRepository;
import com.stockia.stockia.services.SalesReportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * Implementación del servicio de reportes de ventas.
 * Genera análisis completos de ventas con métricas y comparaciones temporales.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class SalesReportServiceImpl implements SalesReportService {

        private final OrderRepository orderRepository;

        @Override
        public SalesReportResponseDto generateSalesReport(LocalDate startDate, LocalDate endDate, String productName) {
                log.info("Generando reporte de ventas para el período: {} - {}", startDate, endDate);

                if (endDate.isBefore(startDate)) {
                        throw new IllegalArgumentException(
                                        "La fecha de fin no puede ser anterior a la fecha de inicio");
                }

                LocalDateTime startDateTime = startDate.atStartOfDay();
                LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);

                SalesMetricsDto currentMetrics = orderRepository.calculateSalesMetrics(
                                startDateTime, endDateTime, productName);

                long daysBetween = ChronoUnit.DAYS.between(startDate, endDate) + 1;
                LocalDate previousStartDate = startDate.minusDays(daysBetween);
                LocalDate previousEndDate = startDate.minusDays(1);

                LocalDateTime previousStartDateTime = previousStartDate.atStartOfDay();
                LocalDateTime previousEndDateTime = previousEndDate.atTime(LocalTime.MAX);

                SalesMetricsDto previousMetrics = orderRepository.calculateSalesMetrics(
                                previousStartDateTime, previousEndDateTime, productName);

                calculateChangePercentages(currentMetrics, previousMetrics);

                List<DailySalesDto> dailySales = orderRepository.getDailySales(
                                startDateTime, endDateTime, productName);

                List<PaymentMethodDistributionDto> paymentDistribution = orderRepository
                                .getPaymentMethodDistribution(startDateTime, endDateTime, productName);

                calculateDistributionPercentages(paymentDistribution);

                SalesReportResponseDto response = SalesReportResponseDto.builder()
                                .metrics(currentMetrics)
                                .dailySales(dailySales)
                                .paymentMethodDistribution(paymentDistribution)
                                .startDate(startDate)
                                .endDate(endDate)
                                .productName(productName)
                                .build();

                log.info("Reporte generado exitosamente. Total ingresos: {}, Total ventas: {}",
                                currentMetrics.getTotalRevenue(), currentMetrics.getTotalOrders());

                return response;
        }

        /**
         * Calcula los porcentajes de cambio entre el período actual y el anterior.
         *
         * @param current  Métricas del período actual
         * @param previous Métricas del período anterior
         */
        private void calculateChangePercentages(SalesMetricsDto current, SalesMetricsDto previous) {
                current.setRevenueChangePercentage(
                                calculatePercentageChange(
                                                previous.getTotalRevenue(),
                                                current.getTotalRevenue()));

                current.setOrdersChangePercentage(
                                calculatePercentageChange(
                                                BigDecimal.valueOf(previous.getTotalOrders()),
                                                BigDecimal.valueOf(current.getTotalOrders())));

                current.setAverageTicketChangePercentage(
                                calculatePercentageChange(
                                                previous.getAverageTicket(),
                                                current.getAverageTicket()));
        }

        /**
         * Calcula el porcentaje de cambio entre dos valores.
         *
         * @param oldValue Valor anterior
         * @param newValue Valor nuevo
         * @return Porcentaje de cambio (positivo = crecimiento, negativo =
         *         decrecimiento)
         */
        private Double calculatePercentageChange(BigDecimal oldValue, BigDecimal newValue) {
                if (oldValue == null || oldValue.compareTo(BigDecimal.ZERO) == 0) {
                        return newValue != null && newValue.compareTo(BigDecimal.ZERO) > 0 ? 100.0 : 0.0;
                }

                if (newValue == null) {
                        newValue = BigDecimal.ZERO;
                }

                BigDecimal difference = newValue.subtract(oldValue);
                BigDecimal percentageChange = difference
                                .divide(oldValue, 4, RoundingMode.HALF_UP)
                                .multiply(BigDecimal.valueOf(100));

                return percentageChange.doubleValue();
        }

        /**
         * Calcula los porcentajes de distribución para cada método de pago.
         *
         * @param distribution Lista de distribución por método de pago
         */
        private void calculateDistributionPercentages(
                        List<PaymentMethodDistributionDto> distribution) {

                BigDecimal total = distribution.stream()
                                .map(PaymentMethodDistributionDto::getTotalAmount)
                                .reduce(BigDecimal.ZERO, BigDecimal::add);

                if (total.compareTo(BigDecimal.ZERO) == 0) {
                        distribution.forEach(d -> d.setPercentage(0.0));
                        return;
                }
                distribution.forEach(d -> {
                        BigDecimal percentage = d.getTotalAmount()
                                        .divide(total, 4, RoundingMode.HALF_UP)
                                        .multiply(BigDecimal.valueOf(100));
                        d.setPercentage(percentage.doubleValue());
                });
        }
}
