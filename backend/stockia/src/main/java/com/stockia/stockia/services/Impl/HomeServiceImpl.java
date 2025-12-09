package com.stockia.stockia.services.Impl;

import com.stockia.stockia.dtos.HomeStatsResponseDto;
import com.stockia.stockia.repositories.ClientRepository;
import com.stockia.stockia.repositories.OrderRepository;
import com.stockia.stockia.repositories.ProductRepository;
import com.stockia.stockia.services.HomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class HomeServiceImpl implements HomeService {
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    @Override
    public HomeStatsResponseDto getStats() {
        LocalDateTime startOfWeek = LocalDate.now()
                .with(java.time.DayOfWeek.MONDAY)
                .atStartOfDay();

        long lowStock = productRepository.countLowStockProducts();
        long activeProducts = productRepository.countActiveProducts();
        long salesThisWeek = orderRepository.countWeeklySales(startOfWeek);
        long activeClients = orderRepository.countClientsBeingServed();

        return new HomeStatsResponseDto(
                lowStock,
                activeProducts,
                salesThisWeek,
                activeClients
        );
    }
}
