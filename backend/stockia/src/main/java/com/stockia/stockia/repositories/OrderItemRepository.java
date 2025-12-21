package com.stockia.stockia.repositories;

import com.stockia.stockia.dtos.report.MonthlyCostDto;
import com.stockia.stockia.dtos.report.MostSoldProductDto;
import com.stockia.stockia.models.OrderItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, UUID> {

    List<OrderItem> findByOrderId(UUID orderId);

    List<OrderItem> findByProductId(UUID productId);

    @Query("""
            SELECT new com.stockia.stockia.dtos.report.MostSoldProductDto(
                p.id,
                p.name,
                pc.name,
                p.price,
                COALESCE(
                    (SELECT SUM(CASE
                        WHEN im.movementType = com.stockia.stockia.enums.MovementType.IN THEN im.quantity
                        WHEN im.movementType = com.stockia.stockia.enums.MovementType.OUT THEN -im.quantity
                        ELSE 0
                    END)
                    FROM com.stockia.stockia.models.InventoryMovement im
                    WHERE im.product.id = p.id
                        AND im.createdAt < :startDate),
                    0
                ) + p.currentStock,
                SUM(oi.quantity),
                p.currentStock
            )
            FROM OrderItem oi
            JOIN oi.product p
            JOIN p.category pc
            JOIN oi.order o
            WHERE o.orderDate BETWEEN :startDate AND :endDate
                AND (o.status = com.stockia.stockia.enums.OrderStatus.CONFIRMED OR o.status = com.stockia.stockia.enums.OrderStatus.DELIVERED)
            GROUP BY p.id, p.name, pc.name, p.price, p.currentStock
            ORDER BY SUM(oi.quantity) DESC
            """)
    Page<MostSoldProductDto> findMostSoldProducts(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            Pageable pageable);

    @Query("""
            SELECT new com.stockia.stockia.dtos.report.MonthlyCostDto(
                MONTH(o.orderDate),
                SUM(oi.quantity),
                AVG(oi.unitPrice)
            )
            FROM OrderItem oi
            JOIN oi.order o
            JOIN oi.product p
            WHERE YEAR(o.orderDate) = :year
                AND (o.status = com.stockia.stockia.enums.OrderStatus.CONFIRMED
                     OR o.status = com.stockia.stockia.enums.OrderStatus.DELIVERED)
                AND (:categoryId IS NULL OR p.category.id = :categoryId)
                AND (:productId IS NULL OR p.id = :productId)
            GROUP BY MONTH(o.orderDate)
            ORDER BY MONTH(o.orderDate)
            """)
    List<MonthlyCostDto> findMonthlySalesData(
            @Param("year") Integer year,
            @Param("categoryId") UUID categoryId,
            @Param("productId") UUID productId);
}
