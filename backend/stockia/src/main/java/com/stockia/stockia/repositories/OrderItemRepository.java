package com.stockia.stockia.repositories;

import com.stockia.stockia.models.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Repositorio para gestionar las operaciones de persistencia de items de
 * órdenes.
 *
 * @author StockIA Team
 * @version 1.0
 * @since 2025-11-24
 */
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
}
