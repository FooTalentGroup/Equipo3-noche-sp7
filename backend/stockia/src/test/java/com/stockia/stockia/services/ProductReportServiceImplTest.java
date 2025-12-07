package com.stockia.stockia.services;

import com.stockia.stockia.dtos.report.MonthlyCostDto;
import com.stockia.stockia.dtos.report.MostSoldProductDto;
import com.stockia.stockia.repositories.InventoryMovementRepository;
import com.stockia.stockia.repositories.OrderItemRepository;
import com.stockia.stockia.services.Impl.ProductReportServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * Tests unitarios para ProductReportServiceImpl.
 * Verifica la correcta conversión de fechas y llamada al repositorio.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("ProductReportService Tests")
class ProductReportServiceImplTest {

        @Mock
        private OrderItemRepository orderItemRepository;

        @Mock
        private InventoryMovementRepository inventoryMovementRepository;

        @InjectMocks
        private ProductReportServiceImpl productReportService;

        private LocalDate startDate;
        private LocalDate endDate;
        private Pageable pageable;
        private List<MostSoldProductDto> mockProducts;

        @BeforeEach
        void setUp() {
                startDate = LocalDate.of(2025, 11, 1);
                endDate = LocalDate.of(2025, 12, 7);
                pageable = PageRequest.of(0, 10);

                // Crear datos de prueba
                mockProducts = List.of(
                                new MostSoldProductDto(
                                                UUID.randomUUID(),
                                                "Laptop HP Pavilion",
                                                "Electrónica",
                                                new BigDecimal("1250.00"),
                                                50L,
                                                25L,
                                                25),
                                new MostSoldProductDto(
                                                UUID.randomUUID(),
                                                "Mouse Logitech",
                                                "Accesorios",
                                                new BigDecimal("35.00"),
                                                100L,
                                                45L,
                                                55));
        }

        @Test
        @DisplayName("Should convert LocalDate to LocalDateTime correctly")
        void shouldConvertLocalDateToLocalDateTimeCorrectly() {
                // Arrange
                Page<MostSoldProductDto> mockPage = new PageImpl<>(mockProducts, pageable, mockProducts.size());
                when(orderItemRepository.findMostSoldProducts(any(LocalDateTime.class), any(LocalDateTime.class),
                                eq(pageable)))
                                .thenReturn(mockPage);

                productReportService.getMostSoldProducts(startDate, endDate, pageable);

                ArgumentCaptor<LocalDateTime> startDateTimeCaptor = ArgumentCaptor.forClass(LocalDateTime.class);
                ArgumentCaptor<LocalDateTime> endDateTimeCaptor = ArgumentCaptor.forClass(LocalDateTime.class);

                verify(orderItemRepository).findMostSoldProducts(
                                startDateTimeCaptor.capture(),
                                endDateTimeCaptor.capture(),
                                eq(pageable));

                // Verificar que startDate se convirtió al inicio del día
                LocalDateTime expectedStartDateTime = startDate.atStartOfDay();
                assertThat(startDateTimeCaptor.getValue()).isEqualTo(expectedStartDateTime);

                // Verificar que endDate se convirtió al final del día
                LocalDateTime expectedEndDateTime = endDate.atTime(LocalTime.MAX);
                assertThat(endDateTimeCaptor.getValue()).isEqualTo(expectedEndDateTime);
        }

        @Test
        @DisplayName("Should return paginated results from repository")
        void shouldReturnPaginatedResultsFromRepository() {
                Page<MostSoldProductDto> mockPage = new PageImpl<>(mockProducts, pageable, mockProducts.size());
                when(orderItemRepository.findMostSoldProducts(any(LocalDateTime.class), any(LocalDateTime.class),
                                eq(pageable)))
                                .thenReturn(mockPage);

                Page<MostSoldProductDto> result = productReportService.getMostSoldProducts(startDate, endDate,
                                pageable);

                assertThat(result).isNotNull();
                assertThat(result.getContent()).hasSize(2);
                assertThat(result.getTotalElements()).isEqualTo(2);
                assertThat(result.getNumber()).isEqualTo(0);
                assertThat(result.getContent().get(0).productName()).isEqualTo("Laptop HP Pavilion");
                assertThat(result.getContent().get(1).productName()).isEqualTo("Mouse Logitech");
        }

        @Test
        @DisplayName("Should call repository with correct parameters")
        void shouldCallRepositoryWithCorrectParameters() {
                Page<MostSoldProductDto> mockPage = new PageImpl<>(mockProducts, pageable, mockProducts.size());
                when(orderItemRepository.findMostSoldProducts(any(LocalDateTime.class), any(LocalDateTime.class),
                                eq(pageable)))
                                .thenReturn(mockPage);

                productReportService.getMostSoldProducts(startDate, endDate, pageable);

                verify(orderItemRepository, times(1)).findMostSoldProducts(
                                any(LocalDateTime.class),
                                any(LocalDateTime.class),
                                eq(pageable));
        }

        @Test
        @DisplayName("Should return empty page when no products found")
        void shouldReturnEmptyPageWhenNoProductsFound() {
                Page<MostSoldProductDto> emptyPage = new PageImpl<>(List.of(), pageable, 0);
                when(orderItemRepository.findMostSoldProducts(any(LocalDateTime.class), any(LocalDateTime.class),
                                eq(pageable)))
                                .thenReturn(emptyPage);

                Page<MostSoldProductDto> result = productReportService.getMostSoldProducts(startDate, endDate,
                                pageable);

                assertThat(result).isNotNull();
                assertThat(result.getContent()).isEmpty();
                assertThat(result.getTotalElements()).isZero();
        }

        @Test
        @DisplayName("Should handle same start and end date")
        void shouldHandleSameStartAndEndDate() {
                LocalDate sameDate = LocalDate.of(2025, 12, 7);
                Page<MostSoldProductDto> mockPage = new PageImpl<>(mockProducts, pageable, mockProducts.size());
                when(orderItemRepository.findMostSoldProducts(any(LocalDateTime.class), any(LocalDateTime.class),
                                eq(pageable)))
                                .thenReturn(mockPage);

                Page<MostSoldProductDto> result = productReportService.getMostSoldProducts(sameDate, sameDate,
                                pageable);

                assertThat(result).isNotNull();
                assertThat(result.getContent()).hasSize(2);

                ArgumentCaptor<LocalDateTime> startDateTimeCaptor = ArgumentCaptor.forClass(LocalDateTime.class);
                ArgumentCaptor<LocalDateTime> endDateTimeCaptor = ArgumentCaptor.forClass(LocalDateTime.class);

                verify(orderItemRepository).findMostSoldProducts(
                                startDateTimeCaptor.capture(),
                                endDateTimeCaptor.capture(),
                                eq(pageable));

                // Verificar que el mismo día se consulta desde inicio hasta fin del día
                assertThat(startDateTimeCaptor.getValue()).isEqualTo(sameDate.atStartOfDay());
                assertThat(endDateTimeCaptor.getValue()).isEqualTo(sameDate.atTime(LocalTime.MAX));
        }

        // ==================== TESTS PARA REPORTE DE COSTOS ====================

        @Test
        @DisplayName("getCostReport - Should return 12 months with correct calculations")
        void getCostReportShouldReturn12MonthsWithCorrectCalculations() {
                Integer year = 2025;
                UUID categoryId = null;
                UUID productId = null;

                // Mock datos de ventas para enero y febrero
                MonthlyCostDto januaryData = new MonthlyCostDto(1, 100L, 150.0);
                MonthlyCostDto februaryData = new MonthlyCostDto(2, 120L, 155.0);
                List<MonthlyCostDto> salesData = List.of(januaryData, februaryData);

                when(orderItemRepository.findMonthlySalesData(year, categoryId, productId))
                                .thenReturn(salesData);

                when(inventoryMovementRepository.findAverageCostByMonth(productId, categoryId, year, 1))
                                .thenReturn(new BigDecimal("50.00"));
                when(inventoryMovementRepository.findAverageCostByMonth(productId, categoryId, year, 2))
                                .thenReturn(new BigDecimal("52.00"));

                // Meses sin datos retornan null
                for (int month = 3; month <= 12; month++) {
                        when(inventoryMovementRepository.findAverageCostByMonth(productId, categoryId, year, month))
                                        .thenReturn(null);
                }

                List<MonthlyCostDto> result = productReportService.getCostReport(year, categoryId, productId);

                assertThat(result).hasSize(12);

                // Verificar enero (primer mes, variación = 0)
                MonthlyCostDto january = result.get(0);
                assertThat(january.month()).isEqualTo(1);
                assertThat(january.monthName()).isEqualTo("Enero");
                assertThat(january.unitsSold()).isEqualTo(100L);
                assertThat(january.avgUnitCost()).isEqualByComparingTo("50.00");
                assertThat(january.totalAvgCost()).isEqualByComparingTo("5000.00"); // 100 * 50
                assertThat(january.costVariationPercent()).isEqualByComparingTo("0.0");

                // Verificar febrero (variación vs enero)
                MonthlyCostDto february = result.get(1);
                assertThat(february.month()).isEqualTo(2);
                assertThat(february.monthName()).isEqualTo("Febrero");
                assertThat(february.unitsSold()).isEqualTo(120L);
                assertThat(february.avgUnitCost()).isEqualByComparingTo("52.00");
                assertThat(february.totalAvgCost()).isEqualByComparingTo("6240.00"); // 120 * 52
                // Variación: ((6240 - 5000) / 5000) * 100 = 24.8%
                assertThat(february.costVariationPercent()).isEqualByComparingTo("24.8");

                // Verificar marzo (sin datos)
                MonthlyCostDto march = result.get(2);
                assertThat(march.month()).isEqualTo(3);
                assertThat(march.monthName()).isEqualTo("Marzo");
                assertThat(march.unitsSold()).isZero();
                assertThat(march.avgUnitCost()).isEqualByComparingTo("0.00");
                assertThat(march.totalAvgCost()).isEqualByComparingTo("0.00");
        }

        @Test
        @DisplayName("getCostReport - Should handle month with no data between months with data")
        void getCostReportShouldHandleMonthWithNoDataBetweenMonthsWithData() {
                Integer year = 2025;
                UUID categoryId = null;
                UUID productId = null;

                // Enero y marzo tienen datos, febrero no
                MonthlyCostDto januaryData = new MonthlyCostDto(1, 100L, 150.0);
                MonthlyCostDto marchData = new MonthlyCostDto(3, 150L, 160.0);
                List<MonthlyCostDto> salesData = List.of(januaryData, marchData);

                when(orderItemRepository.findMonthlySalesData(year, categoryId, productId))
                                .thenReturn(salesData);

                when(inventoryMovementRepository.findAverageCostByMonth(productId, categoryId, year, 1))
                                .thenReturn(new BigDecimal("50.00"));
                when(inventoryMovementRepository.findAverageCostByMonth(productId, categoryId, year, 2))
                                .thenReturn(null);
                when(inventoryMovementRepository.findAverageCostByMonth(productId, categoryId, year, 3))
                                .thenReturn(new BigDecimal("48.00"));

                for (int month = 4; month <= 12; month++) {
                        when(inventoryMovementRepository.findAverageCostByMonth(productId, categoryId, year, month))
                                        .thenReturn(null);
                }

                List<MonthlyCostDto> result = productReportService.getCostReport(year, categoryId, productId);

                MonthlyCostDto january = result.get(0);
                assertThat(january.totalAvgCost()).isEqualByComparingTo("5000.00"); // 100 * 50

                // Febrero no tiene ventas, variación = 0
                MonthlyCostDto february = result.get(1);
                assertThat(february.unitsSold()).isZero();
                assertThat(february.costVariationPercent()).isEqualByComparingTo("0.0");

                // Marzo se compara con enero (último mes con ventas)
                MonthlyCostDto march = result.get(2);
                assertThat(march.totalAvgCost()).isEqualByComparingTo("7200.00"); // 150 * 48
                // Variación: ((7200 - 5000) / 5000) * 100 = 44.0%
                assertThat(march.costVariationPercent()).isEqualByComparingTo("44.0");
        }

        @Test
        @DisplayName("getCostReport - Should filter by category")
        void getCostReportShouldFilterByCategory() {
                Integer year = 2025;
                UUID categoryId = UUID.randomUUID();
                UUID productId = null;

                when(orderItemRepository.findMonthlySalesData(year, categoryId, productId))
                                .thenReturn(List.of());

                for (int month = 1; month <= 12; month++) {
                        when(inventoryMovementRepository.findAverageCostByMonth(productId, categoryId, year, month))
                                        .thenReturn(null);
                }

                List<MonthlyCostDto> result = productReportService.getCostReport(year, categoryId, productId);

                verify(orderItemRepository).findMonthlySalesData(eq(year), eq(categoryId), eq(productId));
                verify(inventoryMovementRepository, times(12)).findAverageCostByMonth(
                                eq(productId), eq(categoryId), eq(year), anyInt());
                assertThat(result).hasSize(12);
        }

        @Test
        @DisplayName("getCostReport - Should filter by product")
        void getCostReportShouldFilterByProduct() {
                Integer year = 2025;
                UUID categoryId = null;
                UUID productId = UUID.randomUUID();

                when(orderItemRepository.findMonthlySalesData(year, categoryId, productId))
                                .thenReturn(List.of());

                for (int month = 1; month <= 12; month++) {
                        when(inventoryMovementRepository.findAverageCostByMonth(productId, categoryId, year, month))
                                        .thenReturn(null);
                }

                List<MonthlyCostDto> result = productReportService.getCostReport(year, categoryId, productId);

                verify(orderItemRepository).findMonthlySalesData(eq(year), eq(categoryId), eq(productId));
                verify(inventoryMovementRepository, times(12)).findAverageCostByMonth(
                                eq(productId), eq(categoryId), eq(year), anyInt());
                assertThat(result).hasSize(12);
        }

        @Test
        @DisplayName("getCostReport - Should return all zeros when no data")
        void getCostReportShouldReturnAllZerosWhenNoData() {
                Integer year = 2025;
                UUID categoryId = null;
                UUID productId = null;

                when(orderItemRepository.findMonthlySalesData(year, categoryId, productId))
                                .thenReturn(List.of());

                for (int month = 1; month <= 12; month++) {
                        when(inventoryMovementRepository.findAverageCostByMonth(productId, categoryId, year, month))
                                        .thenReturn(null);
                }

                List<MonthlyCostDto> result = productReportService.getCostReport(year, categoryId, productId);

                assertThat(result).hasSize(12);
                result.forEach(month -> {
                        assertThat(month.unitsSold()).isZero();
                        assertThat(month.avgUnitCost()).isEqualByComparingTo("0.00");
                        assertThat(month.totalAvgCost()).isEqualByComparingTo("0.00");
                        assertThat(month.costVariationPercent()).isEqualByComparingTo("0.0");
                });
        }

        @Test
        @DisplayName("getCostReport - Should have correct month names in Spanish")
        void getCostReportShouldHaveCorrectMonthNamesInSpanish() {
                Integer year = 2025;
                when(orderItemRepository.findMonthlySalesData(year, null, null))
                                .thenReturn(List.of());

                for (int month = 1; month <= 12; month++) {
                        when(inventoryMovementRepository.findAverageCostByMonth(null, null, year, month))
                                        .thenReturn(null);
                }

                String[] expectedMonthNames = {
                                "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
                                "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"
                };

                List<MonthlyCostDto> result = productReportService.getCostReport(year, null, null);

                for (int i = 0; i < 12; i++) {
                        assertThat(result.get(i).month()).isEqualTo(i + 1);
                        assertThat(result.get(i).monthName()).isEqualTo(expectedMonthNames[i]);
                }
        }
}
