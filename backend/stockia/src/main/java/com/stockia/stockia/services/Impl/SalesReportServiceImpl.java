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

                // Validar rango de fechas
                if (endDate.isBefore(startDate)) {
                        throw new IllegalArgumentException(
                                        "La fecha de fin no puede ser anterior a la fecha de inicio");
                }

                // Convertir fechas a LocalDateTime para las queries
                LocalDateTime startDateTime = startDate.atStartOfDay();
                LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);

                // Obtener métricas del período actual
                SalesMetricsDto currentMetrics = orderRepository.calculateSalesMetrics(
                                startDateTime, endDateTime, productName);

                // Calcular métricas del período anterior para comparación
                long daysBetween = ChronoUnit.DAYS.between(startDate, endDate) + 1;
                LocalDate previousStartDate = startDate.minusDays(daysBetween);
                LocalDate previousEndDate = startDate.minusDays(1);

                LocalDateTime previousStartDateTime = previousStartDate.atStartOfDay();
                LocalDateTime previousEndDateTime = previousEndDate.atTime(LocalTime.MAX);

                SalesMetricsDto previousMetrics = orderRepository.calculateSalesMetrics(
                                previousStartDateTime, previousEndDateTime, productName);

                // Calcular porcentajes de cambio
                calculateChangePercentages(currentMetrics, previousMetrics);

                // Obtener ventas diarias
                List<DailySalesDto> dailySales = orderRepository.getDailySales(
                                startDateTime, endDateTime, productName);

                // Obtener distribución por método de pago
                List<PaymentMethodDistributionDto> paymentDistribution = orderRepository
                                .getPaymentMethodDistribution(startDateTime, endDateTime, productName);

                // Calcular porcentajes para la distribución
                calculateDistributionPercentages(paymentDistribution);

                // Construir respuesta
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
                // Calcular cambio en ingresos
                current.setRevenueChangePercentage(
                                calculatePercentageChange(
                                                previous.getTotalRevenue(),
                                                current.getTotalRevenue()));

                // Calcular cambio en cantidad de órdenes
                current.setOrdersChangePercentage(
                                calculatePercentageChange(
                                                BigDecimal.valueOf(previous.getTotalOrders()),
                                                BigDecimal.valueOf(current.getTotalOrders())));

                // Calcular cambio en ticket promedio
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
                        // Si el valor anterior es 0, no se puede calcular porcentaje
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

                // Calcular el total
                BigDecimal total = distribution.stream()
                                .map(PaymentMethodDistributionDto::getTotalAmount)
                                .reduce(BigDecimal.ZERO, BigDecimal::add);

                // Si el total es 0, todos los porcentajes son 0
                if (total.compareTo(BigDecimal.ZERO) == 0) {
                        distribution.forEach(d -> d.setPercentage(0.0));
                        return;
                }

                // Calcular porcentaje para cada método de pago
                distribution.forEach(d -> {
                        BigDecimal percentage = d.getTotalAmount()
                                        .divide(total, 4, RoundingMode.HALF_UP)
                                        .multiply(BigDecimal.valueOf(100));
                        d.setPercentage(percentage.doubleValue());
                });
        }
}
