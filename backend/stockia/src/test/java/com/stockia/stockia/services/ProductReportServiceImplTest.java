package com.stockia.stockia.services;

import com.stockia.stockia.dtos.report.MostSoldProductDto;
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

                // Act
                productReportService.getMostSoldProducts(startDate, endDate, pageable);

                // Assert
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
                // Arrange
                Page<MostSoldProductDto> mockPage = new PageImpl<>(mockProducts, pageable, mockProducts.size());
                when(orderItemRepository.findMostSoldProducts(any(LocalDateTime.class), any(LocalDateTime.class),
                                eq(pageable)))
                                .thenReturn(mockPage);

                // Act
                Page<MostSoldProductDto> result = productReportService.getMostSoldProducts(startDate, endDate,
                                pageable);

                // Assert
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
                // Arrange
                Page<MostSoldProductDto> mockPage = new PageImpl<>(mockProducts, pageable, mockProducts.size());
                when(orderItemRepository.findMostSoldProducts(any(LocalDateTime.class), any(LocalDateTime.class),
                                eq(pageable)))
                                .thenReturn(mockPage);

                // Act
                productReportService.getMostSoldProducts(startDate, endDate, pageable);

                // Assert
                verify(orderItemRepository, times(1)).findMostSoldProducts(
                                any(LocalDateTime.class),
                                any(LocalDateTime.class),
                                eq(pageable));
        }

        @Test
        @DisplayName("Should return empty page when no products found")
        void shouldReturnEmptyPageWhenNoProductsFound() {
                // Arrange
                Page<MostSoldProductDto> emptyPage = new PageImpl<>(List.of(), pageable, 0);
                when(orderItemRepository.findMostSoldProducts(any(LocalDateTime.class), any(LocalDateTime.class),
                                eq(pageable)))
                                .thenReturn(emptyPage);

                // Act
                Page<MostSoldProductDto> result = productReportService.getMostSoldProducts(startDate, endDate,
                                pageable);

                // Assert
                assertThat(result).isNotNull();
                assertThat(result.getContent()).isEmpty();
                assertThat(result.getTotalElements()).isZero();
        }

        @Test
        @DisplayName("Should handle same start and end date")
        void shouldHandleSameStartAndEndDate() {
                // Arrange
                LocalDate sameDate = LocalDate.of(2025, 12, 7);
                Page<MostSoldProductDto> mockPage = new PageImpl<>(mockProducts, pageable, mockProducts.size());
                when(orderItemRepository.findMostSoldProducts(any(LocalDateTime.class), any(LocalDateTime.class),
                                eq(pageable)))
                                .thenReturn(mockPage);

                // Act
                Page<MostSoldProductDto> result = productReportService.getMostSoldProducts(sameDate, sameDate,
                                pageable);

                // Assert
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
}
