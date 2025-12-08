package com.stockia.stockia.repositories;

import com.stockia.stockia.dtos.report.DailyMovementDto;
import com.stockia.stockia.enums.MovementType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.stockia.stockia.models.InventoryMovement;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
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

        /**
         * Obtiene movimientos diarios agrupados por fecha y tipo.
         * Usado para el reporte de stock.
         * 
         * @param productId ID del producto
         * @param startDate Fecha de inicio
         * @param endDate   Fecha de fin
         * @return Lista de movimientos agrupados por día y tipo
         */
        @Query("""
                        SELECT new com.stockia.stockia.dtos.report.DailyMovementDto(
                            CAST(im.createdAt AS LocalDate),
                            im.movementType,
                            SUM(im.quantity)
                        )
                        FROM InventoryMovement im
                        WHERE im.product.id = :productId
                            AND CAST(im.createdAt AS LocalDate) BETWEEN :startDate AND :endDate
                        GROUP BY CAST(im.createdAt AS LocalDate), im.movementType
                        ORDER BY CAST(im.createdAt AS LocalDate)
                        """)
        List<DailyMovementDto> findDailyMovementsByProduct(
                        @Param("productId") UUID productId,
                        @Param("startDate") LocalDate startDate,
                        @Param("endDate") LocalDate endDate);

        /**
         * Calcula el stock de un producto antes de una fecha específica.
         * Suma todos los movimientos IN y resta todos los OUT antes de la fecha.
         * 
         * @param productId  ID del producto
         * @param beforeDate Fecha límite (no incluida)
         * @return Stock calculado antes de la fecha, 0 si no hay movimientos
         */
        @Query("""
                        SELECT COALESCE(SUM(
                            CASE
                                WHEN im.movementType = com.stockia.stockia.enums.MovementType.IN THEN im.quantity
                                WHEN im.movementType = com.stockia.stockia.enums.MovementType.OUT THEN -im.quantity
                                ELSE 0
                            END
                        ), 0)
                        FROM InventoryMovement im
                        WHERE im.product.id = :productId
                            AND CAST(im.createdAt AS LocalDate) < :beforeDate
                        """)
        Integer calculateStockBeforeDate(
                        @Param("productId") UUID productId,
                        @Param("beforeDate") LocalDate beforeDate);
}