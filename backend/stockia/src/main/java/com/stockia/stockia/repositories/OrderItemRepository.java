package com.stockia.stockia.repositories;

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

    /**
     * Busca todos los items de una orden específica.
     * 
     * @param orderId ID de la orden
     * @return Lista de items de la orden
     */
    List<OrderItem> findByOrderId(UUID orderId);

    /**
     * Busca todos los items que contienen un producto específico.
     * Útil para reportes y análisis de ventas por producto.
     * 
     * @param productId ID del producto
     * @return Lista de items que contienen el producto
     */
    List<OrderItem> findByProductId(UUID productId);

    /**
     * Obtiene los productos más vendidos en un rango de fechas.
     * 
     * Calcula la cantidad inicial mediante una subconsulta que suma todos los
     * movimientos de inventario (IN y OUT) ocurridos antes de la fecha de inicio,
     * y se suma al stock actual para obtener el stock que había al inicio del
     * periodo.
     * 
     * Solo incluye órdenes con estado CONFIRMED o DELIVERED.
     * Los resultados están ordenados por cantidad vendida descendente.
     * 
     * @param startDate Fecha y hora de inicio del periodo (inclusive)
     * @param endDate   Fecha y hora de fin del periodo (inclusive)
     * @param pageable  Configuración de paginación y ordenamiento
     * @return Página con los productos más vendidos y sus estadísticas
     */
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
}
