package com.stockia.stockia.repositories;

import com.stockia.stockia.enums.MovementType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.stockia.stockia.models.InventoryMovement;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Repository
public interface InventoryMovementRepository extends JpaRepository<InventoryMovement, UUID> {
    @Query("""
                SELECT i FROM InventoryMovement i
                WHERE
                    (:productId IS NULL OR i.product.id = :productId) AND
                    (:productName IS NULL OR :productName = '' OR LOWER(i.product.name) LIKE LOWER(CONCAT('%', :productName, '%'))) AND
                    (:movementType IS NULL OR i.movementType = :movementType) AND
                    (:userId IS NULL OR i.user.id = :userId) AND
                    (CAST(:startDate AS timestamp) IS NULL OR i.createdAt >= :startDate) AND
                    (CAST(:endDate AS timestamp) IS NULL OR i.createdAt <= :endDate)
            """)
    Page<InventoryMovement> searchInventoryMovements(
            @Param("productId") UUID productId,
            @Param("productName") String productName,
            @Param("movementType") MovementType movementType,
            @Param("userId") UUID userId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            Pageable pageable);

    /**
     * Calcula el costo unitario promedio de un producto en un mes específico.
     * Solo considera movimientos de tipo IN (compras).
     * 
     * @param productId  ID del producto (puede ser null para todos)
     * @param categoryId ID de categoría (puede ser null para todas)
     * @param year       Año
     * @param month      Mes (1-12)
     * @return Costo promedio o null si no hay movimientos
     */
    @Query("""
            SELECT AVG(im.purchaseCost)
            FROM InventoryMovement im
            WHERE im.movementType = com.stockia.stockia.enums.MovementType.IN
                AND YEAR(im.createdAt) = :year
                AND MONTH(im.createdAt) = :month
                AND (:productId IS NULL OR im.product.id = :productId)
                AND (:categoryId IS NULL OR im.product.category.id = :categoryId)
            """)
    BigDecimal findAverageCostByMonth(
            @Param("productId") UUID productId,
            @Param("categoryId") UUID categoryId,
            @Param("year") Integer year,
            @Param("month") Integer month);
}