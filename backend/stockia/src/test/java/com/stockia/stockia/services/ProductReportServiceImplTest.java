package com.stockia.stockia.services;

import com.stockia.stockia.dtos.report.DailyMovementDto;
import com.stockia.stockia.dtos.report.DailyStockDto;
import com.stockia.stockia.dtos.report.MonthlyCostDto;
import com.stockia.stockia.dtos.report.MostSoldProductDto;
import com.stockia.stockia.enums.MovementType;
import com.stockia.stockia.models.Product;
import com.stockia.stockia.repositories.InventoryMovementRepository;
import com.stockia.stockia.repositories.OrderItemRepository;
import com.stockia.stockia.repositories.ProductRepository;
import com.stockia.stockia.services.Impl.ProductReportServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
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

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("ProductReportService Tests")
class ProductReportServiceImplTest {

        @Mock
        private OrderItemRepository orderItemRepository;

        @Mock
        private InventoryMovementRepository inventoryMovementRepository;

        @Mock
        private ProductRepository productRepository;

        @Mock
        private com.stockia.stockia.repositories.ProductCategoryRepository categoryRepository;

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

                mockProducts = List.of(
                                new MostSoldProductDto(
                                                UUID.randomUUID(),
                                                "helado de vainilla",
                                                "Postres",
                                                new BigDecimal("10.00"),
                                                50L,
                                                25L,
                                                25),
                                new MostSoldProductDto(
                                                UUID.randomUUID(),
                                                "helado de chocolate",
                                                "Postres",
                                                new BigDecimal("10.00"),
                                                100L,
                                                45L,
                                                55));
        }

        @Test
        @DisplayName("Should convert LocalDate to LocalDateTime correctly")
        void shouldConvertLocalDateToLocalDateTimeCorrectly() {
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

                LocalDateTime expectedStartDateTime = startDate.atStartOfDay();
                assertThat(startDateTimeCaptor.getValue()).isEqualTo(expectedStartDateTime);

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
                assertThat(result.getContent().get(0).productName()).isEqualTo("helado de vainilla");
                assertThat(result.getContent().get(1).productName()).isEqualTo("helado de chocolate");
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

                assertThat(startDateTimeCaptor.getValue()).isEqualTo(sameDate.atStartOfDay());
                assertThat(endDateTimeCaptor.getValue()).isEqualTo(sameDate.atTime(LocalTime.MAX));
        }

        @Test
        @DisplayName("getCostReport - Should return 12 months with correct calculations")
        void getCostReportShouldReturn12MonthsWithCorrectCalculations() {
                Integer year = 2025;
                String categoryName = null;
                String productName = null;
                UUID categoryId = null;
                UUID productId = null;

                MonthlyCostDto januaryData = new MonthlyCostDto(1, 100L, 150.0);
                MonthlyCostDto februaryData = new MonthlyCostDto(2, 120L, 155.0);
                List<MonthlyCostDto> salesData = List.of(januaryData, februaryData);

                when(orderItemRepository.findMonthlySalesData(year, categoryId, productId))
                                .thenReturn(salesData);

                when(inventoryMovementRepository.findAverageCostByMonth(productId, categoryId, year, 1))
                                .thenReturn(new BigDecimal("50.00"));
                when(inventoryMovementRepository.findAverageCostByMonth(productId, categoryId, year, 2))
                                .thenReturn(new BigDecimal("52.00"));

                for (int month = 3; month <= 12; month++) {
                        when(inventoryMovementRepository.findAverageCostByMonth(productId, categoryId, year, month))
                                        .thenReturn(null);
                }

                List<MonthlyCostDto> result = productReportService.getCostReport(year, categoryName, productName);

                assertThat(result).hasSize(12);

                MonthlyCostDto january = result.get(0);
                assertThat(january.month()).isEqualTo(1);
                assertThat(january.monthName()).isEqualTo("Enero");
                assertThat(january.unitsSold()).isEqualTo(100L);
                assertThat(january.avgUnitCost()).isEqualByComparingTo("50.00");
                assertThat(january.totalAvgCost()).isEqualByComparingTo("5000.00");
                assertThat(january.costVariationPercent()).isEqualByComparingTo("0.0");

                MonthlyCostDto february = result.get(1);
                assertThat(february.month()).isEqualTo(2);
                assertThat(february.monthName()).isEqualTo("Febrero");
                assertThat(february.unitsSold()).isEqualTo(120L);
                assertThat(february.avgUnitCost()).isEqualByComparingTo("52.00");
                assertThat(february.totalAvgCost()).isEqualByComparingTo("6240.00");
                assertThat(february.costVariationPercent()).isEqualByComparingTo("24.8");

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

                List<MonthlyCostDto> result = productReportService.getCostReport(year, null, null);

                MonthlyCostDto january = result.get(0);
                assertThat(january.totalAvgCost()).isEqualByComparingTo("5000.00");

                MonthlyCostDto february = result.get(1);
                assertThat(february.unitsSold()).isZero();
                assertThat(february.costVariationPercent()).isEqualByComparingTo("0.0");
                MonthlyCostDto march = result.get(2);
                assertThat(march.totalAvgCost()).isEqualByComparingTo("7200.00");
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

                List<MonthlyCostDto> result = productReportService.getCostReport(year, null, null);

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

                List<MonthlyCostDto> result = productReportService.getCostReport(year, null, null);

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

                List<MonthlyCostDto> result = productReportService.getCostReport(year, null, null);

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

        @Test
        @DisplayName("getStockReport - Should calculate daily stock correctly")
        void getStockReportShouldCalculateDailyStockCorrectly() {
                String productName = "helado de vainilla";
                UUID productId = UUID.randomUUID();
                LocalDate startDate = LocalDate.of(2025, 11, 12);
                LocalDate endDate = LocalDate.of(2025, 11, 14);

                Product product = new Product();
                product.setId(productId);
                product.setName(productName);
                when(productRepository.findByNameContainingIgnoreCaseAndDeletedFalse(productName))
                                .thenReturn(List.of(product));

                when(inventoryMovementRepository.calculateStockBeforeDate(productId, startDate))
                                .thenReturn(100);

                List<DailyMovementDto> movements = List.of(
                                new DailyMovementDto(LocalDate.of(2025, 11, 12), MovementType.OUT, 10L),
                                new DailyMovementDto(LocalDate.of(2025, 11, 13), MovementType.OUT, 9L),
                                new DailyMovementDto(LocalDate.of(2025, 11, 14), MovementType.OUT, 12L));

                when(inventoryMovementRepository.findDailyMovementsByProduct(productId, startDate, endDate))
                                .thenReturn(movements);

                List<DailyStockDto> result = productReportService.getStockReport(productName, startDate, endDate);

                assertThat(result).hasSize(3);

                DailyStockDto day1 = result.get(0);
                assertThat(day1.date()).isEqualTo(LocalDate.of(2025, 11, 12));
                assertThat(day1.initialStock()).isEqualTo(100);
                assertThat(day1.entries()).isEqualTo(0);
                assertThat(day1.exits()).isEqualTo(10);
                assertThat(day1.currentStock()).isEqualTo(90);

                DailyStockDto day2 = result.get(1);
                assertThat(day2.date()).isEqualTo(LocalDate.of(2025, 11, 13));
                assertThat(day2.initialStock()).isEqualTo(90);
                assertThat(day2.exits()).isEqualTo(9);
                assertThat(day2.currentStock()).isEqualTo(81);

                DailyStockDto day3 = result.get(2);
                assertThat(day3.date()).isEqualTo(LocalDate.of(2025, 11, 14));
                assertThat(day3.initialStock()).isEqualTo(81);
                assertThat(day3.exits()).isEqualTo(12);
                assertThat(day3.currentStock()).isEqualTo(69);
        }

        @Test
        @DisplayName("getStockReport - Should include days without movements")
        void getStockReportShouldIncludeDaysWithoutMovements() {
                String productName = "helado de vainilla";
                UUID productId = UUID.randomUUID();
                LocalDate startDate = LocalDate.of(2025, 11, 12);
                LocalDate endDate = LocalDate.of(2025, 11, 16);

                Product product = new Product();
                product.setId(productId);
                product.setName(productName);
                when(productRepository.findByNameContainingIgnoreCaseAndDeletedFalse(productName))
                                .thenReturn(List.of(product));

                when(inventoryMovementRepository.calculateStockBeforeDate(productId, startDate))
                                .thenReturn(100);

                List<DailyMovementDto> movements = List.of(
                                new DailyMovementDto(LocalDate.of(2025, 11, 12), MovementType.OUT, 10L),
                                new DailyMovementDto(LocalDate.of(2025, 11, 16), MovementType.IN, 20L));

                when(inventoryMovementRepository.findDailyMovementsByProduct(productId, startDate, endDate))
                                .thenReturn(movements);

                List<DailyStockDto> result = productReportService.getStockReport(productName, startDate, endDate);

                assertThat(result).hasSize(5);

                for (int day = 13; day <= 15; day++) {
                        DailyStockDto dayData = result.get(day - 12);
                        assertThat(dayData.date()).isEqualTo(LocalDate.of(2025, 11, day));
                        assertThat(dayData.entries()).isZero();
                        assertThat(dayData.exits()).isZero();
                        assertThat(dayData.initialStock()).isEqualTo(90);
                        assertThat(dayData.currentStock()).isEqualTo(90);
                }
        }

        @Test
        @DisplayName("getStockReport - Should calculate variation percentages correctly")
        void getStockReportShouldCalculateVariationPercentagesCorrectly() {
                String productName = "helado de vainilla";
                UUID productId = UUID.randomUUID();
                LocalDate startDate = LocalDate.of(2025, 11, 12);
                LocalDate endDate = LocalDate.of(2025, 11, 14);

                Product product = new Product();
                product.setId(productId);
                product.setName(productName);
                when(productRepository.findByNameContainingIgnoreCaseAndDeletedFalse(productName))
                                .thenReturn(List.of(product));

                when(inventoryMovementRepository.calculateStockBeforeDate(productId, startDate))
                                .thenReturn(100);

                List<DailyMovementDto> movements = List.of(
                                new DailyMovementDto(LocalDate.of(2025, 11, 12), MovementType.OUT, 10L),
                                new DailyMovementDto(LocalDate.of(2025, 11, 13), MovementType.OUT, 9L),
                                new DailyMovementDto(LocalDate.of(2025, 11, 14), MovementType.IN, 19L));

                when(inventoryMovementRepository.findDailyMovementsByProduct(productId, startDate, endDate))
                                .thenReturn(movements);

                List<DailyStockDto> result = productReportService.getStockReport(productName, startDate, endDate);

                assertThat(result.get(0).stockVariationPercent()).isEqualByComparingTo("0.0");

                assertThat(result.get(1).stockVariationPercent()).isEqualByComparingTo("-10.0");

                assertThat(result.get(2).stockVariationPercent()).isEqualByComparingTo("23.5");
        }

        @Test
        @DisplayName("getStockReport - Should handle entries and exits on same day")
        void getStockReportShouldHandleEntriesAndExitsOnSameDay() {
                String productName = "helado de vainilla";
                UUID productId = UUID.randomUUID();
                LocalDate startDate = LocalDate.of(2025, 11, 12);
                LocalDate endDate = LocalDate.of(2025, 11, 12);

                Product product = new Product();
                product.setId(productId);
                product.setName(productName);
                when(productRepository.findByNameContainingIgnoreCaseAndDeletedFalse(productName))
                                .thenReturn(List.of(product));

                when(inventoryMovementRepository.calculateStockBeforeDate(productId, startDate))
                                .thenReturn(100);

                List<DailyMovementDto> movements = List.of(
                                new DailyMovementDto(LocalDate.of(2025, 11, 12), MovementType.IN, 50L),
                                new DailyMovementDto(LocalDate.of(2025, 11, 12), MovementType.OUT, 30L));

                when(inventoryMovementRepository.findDailyMovementsByProduct(productId, startDate, endDate))
                                .thenReturn(movements);

                List<DailyStockDto> result = productReportService.getStockReport(productName, startDate, endDate);

                assertThat(result).hasSize(1);
                DailyStockDto day = result.get(0);
                assertThat(day.initialStock()).isEqualTo(100);
                assertThat(day.entries()).isEqualTo(50);
                assertThat(day.exits()).isEqualTo(30);
                assertThat(day.currentStock()).isEqualTo(120);
        }

        @Test
        @DisplayName("getStockReport - Should handle period without any movements")
        void getStockReportShouldHandlePeriodWithoutAnyMovements() {
                String productName = "helado de vainilla";
                UUID productId = UUID.randomUUID();
                LocalDate startDate = LocalDate.of(2025, 11, 12);
                LocalDate endDate = LocalDate.of(2025, 11, 14);

                Product product = new Product();
                product.setId(productId);
                product.setName(productName);
                when(productRepository.findByNameContainingIgnoreCaseAndDeletedFalse(productName))
                                .thenReturn(List.of(product));

                when(inventoryMovementRepository.calculateStockBeforeDate(productId, startDate))
                                .thenReturn(50);

                when(inventoryMovementRepository.findDailyMovementsByProduct(productId, startDate, endDate))
                                .thenReturn(List.of());

                List<DailyStockDto> result = productReportService.getStockReport(productName, startDate, endDate);

                assertThat(result).hasSize(3);

                result.forEach(day -> {
                        assertThat(day.initialStock()).isEqualTo(50);
                        assertThat(day.entries()).isZero();
                        assertThat(day.exits()).isZero();
                        assertThat(day.currentStock()).isEqualTo(50);
                        assertThat(day.stockVariationPercent()).isEqualByComparingTo("0.0");
                });
        }

        @Test
        @DisplayName("getStockReport - Should calculate initial stock from history")
        void getStockReportShouldCalculateInitialStockFromHistory() {
                String productName = "helado de vainilla";
                UUID productId = UUID.randomUUID();
                LocalDate startDate = LocalDate.of(2025, 11, 12);
                LocalDate endDate = LocalDate.of(2025, 11, 12);

                Product product = new Product();
                product.setId(productId);
                product.setName(productName);
                when(productRepository.findByNameContainingIgnoreCaseAndDeletedFalse(productName))
                                .thenReturn(List.of(product));

                when(inventoryMovementRepository.calculateStockBeforeDate(productId, startDate))
                                .thenReturn(75);

                when(inventoryMovementRepository.findDailyMovementsByProduct(productId, startDate, endDate))
                                .thenReturn(List.of());

                List<DailyStockDto> result = productReportService.getStockReport(productName, startDate, endDate);

                verify(inventoryMovementRepository).calculateStockBeforeDate(eq(productId), eq(startDate));
                assertThat(result.get(0).initialStock()).isEqualTo(75);
        }

        @Test
        @DisplayName("getStockReport - Should call repository methods with correct parameters")
        void getStockReportShouldCallRepositoryMethodsWithCorrectParameters() {
                String productName = "helado de vainilla";
                UUID productId = UUID.randomUUID();
                LocalDate startDate = LocalDate.of(2025, 11, 12);
                LocalDate endDate = LocalDate.of(2025, 11, 14);

                Product product = new Product();
                product.setId(productId);
                product.setName(productName);
                when(productRepository.findByNameContainingIgnoreCaseAndDeletedFalse(productName))
                                .thenReturn(List.of(product));

                when(inventoryMovementRepository.calculateStockBeforeDate(productId, startDate))
                                .thenReturn(100);
                when(inventoryMovementRepository.findDailyMovementsByProduct(productId, startDate, endDate))
                                .thenReturn(List.of());

                productReportService.getStockReport(productName, startDate, endDate);

                verify(inventoryMovementRepository, times(1))
                                .calculateStockBeforeDate(eq(productId), eq(startDate));
                verify(inventoryMovementRepository, times(1))
                                .findDailyMovementsByProduct(eq(productId), eq(startDate), eq(endDate));
        }
}
