package com.stockia.stockia.repositories;

import com.stockia.stockia.dtos.report.DailySalesDto;
import com.stockia.stockia.dtos.report.PaymentMethodDistributionDto;
import com.stockia.stockia.dtos.report.SalesMetricsDto;
import com.stockia.stockia.enums.OrderStatus;
import com.stockia.stockia.enums.PaymentMethod;
import com.stockia.stockia.enums.PaymentStatus;
import com.stockia.stockia.models.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {

        Optional<Order> findByOrderNumber(String orderNumber);

        boolean existsByOrderNumber(String orderNumber);

        List<Order> findByStatusOrderByOrderDateDesc(OrderStatus status);

        List<Order> findByCustomer_IdOrderByOrderDateDesc(UUID customerId);

        List<Order> findByUserIdOrderByOrderDateDesc(UUID userId);


        List<Order> findByOrderDateBetweenOrderByOrderDateDesc(LocalDateTime start, LocalDateTime end);

        List<Order> findAllByOrderByOrderDateDesc();

        @Query("SELECT o FROM Order o LEFT JOIN o.customer c WHERE " +
                        "(:orderNumber IS NULL OR :orderNumber = '' OR LOWER(o.orderNumber) LIKE LOWER(CONCAT('%', :orderNumber, '%'))) AND "
                        +
                        "(:customerName IS NULL OR :customerName = '' OR LOWER(c.name) LIKE LOWER(CONCAT('%', :customerName, '%'))) AND "
                        +
                        "(:status IS NULL OR o.status = :status) AND " +
                        "(:paymentMethod IS NULL OR o.paymentMethod = :paymentMethod) AND " +
                        "(:paymentStatus IS NULL OR o.paymentStatus = :paymentStatus) AND " +
                        "(CAST(:startDate AS timestamp) IS NULL OR o.orderDate >= :startDate) AND " +
                        "(CAST(:endDate AS timestamp) IS NULL OR o.orderDate <= :endDate)")
        Page<Order> searchOrders(
                        @Param("orderNumber") String orderNumber,
                        @Param("customerName") String customerName,
                        @Param("status") OrderStatus status,
                        @Param("paymentMethod") PaymentMethod paymentMethod,
                        @Param("paymentStatus") PaymentStatus paymentStatus,
                        @Param("startDate") LocalDateTime startDate,
                        @Param("endDate") LocalDateTime endDate,
                        Pageable pageable);

        @Query("SELECT new com.stockia.stockia.dtos.report.SalesMetricsDto(" +
                        "COALESCE(SUM(o.totalAmount), 0.0), " +
                        "CAST(COUNT(o) AS long), " +
                        "COALESCE(AVG(o.totalAmount), 0.0)) " +
                        "FROM Order o LEFT JOIN o.items oi LEFT JOIN oi.product p " +
                        "WHERE o.status IN ('CONFIRMED', 'DELIVERED') " +
                        "AND o.orderDate >= :startDate AND o.orderDate <= :endDate " +
                        "AND (:productName IS NULL OR :productName = '' OR LOWER(p.name) LIKE LOWER(CONCAT('%', :productName, '%')))")
        SalesMetricsDto calculateSalesMetrics(
                        @Param("startDate") LocalDateTime startDate,
                        @Param("endDate") LocalDateTime endDate,
                        @Param("productName") String productName);

        @Query("SELECT new com.stockia.stockia.dtos.report.DailySalesDto(" +
                        "CAST(o.orderDate AS LocalDate), " +
                        "COALESCE(SUM(o.totalAmount), 0.0)) " +
                        "FROM Order o LEFT JOIN o.items oi LEFT JOIN oi.product p " +
                        "WHERE o.status IN ('CONFIRMED', 'DELIVERED') " +
                        "AND o.orderDate >= :startDate AND o.orderDate <= :endDate " +
                        "AND (:productName IS NULL OR :productName = '' OR LOWER(p.name) LIKE LOWER(CONCAT('%', :productName, '%'))) "
                        +
                        "GROUP BY CAST(o.orderDate AS LocalDate) " +
                        "ORDER BY CAST(o.orderDate AS LocalDate)")
        List<DailySalesDto> getDailySales(
                        @Param("startDate") LocalDateTime startDate,
                        @Param("endDate") LocalDateTime endDate,
                        @Param("productName") String productName);

        @Query("SELECT new com.stockia.stockia.dtos.report.PaymentMethodDistributionDto(" +
                        "o.paymentMethod, " +
                        "COALESCE(SUM(o.totalAmount), 0.0)) " +
                        "FROM Order o LEFT JOIN o.items oi LEFT JOIN oi.product p " +
                        "WHERE o.status IN ('CONFIRMED', 'DELIVERED') " +
                        "AND o.orderDate >= :startDate AND o.orderDate <= :endDate " +
                        "AND (:productName IS NULL OR :productName = '' OR LOWER(p.name) LIKE LOWER(CONCAT('%', :productName, '%'))) "
                        +
                        "GROUP BY o.paymentMethod")
        List<PaymentMethodDistributionDto> getPaymentMethodDistribution(
                        @Param("startDate") LocalDateTime startDate,
                        @Param("endDate") LocalDateTime endDate,
                        @Param("productName") String productName);

        @Query("""
                    SELECT COUNT(o)
                    FROM Order o
                    WHERE o.status IN ('CONFIRMED', 'DELIVERED')
                      AND o.createdAt >= :startOfWeek
        """)
        long countWeeklySales(@Param("startOfWeek") LocalDateTime startOfWeek);

        @Query("""
            SELECT COUNT(DISTINCT o.customer.id)
            FROM Order o
            WHERE o.status IN ('PENDING', 'CONFIRMED')
        """)
        long countClientsBeingServed();
}