package com.stockia.stockia.repositories;

import com.stockia.stockia.enums.OrderStatus;
import com.stockia.stockia.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repositorio para gestionar las operaciones de persistencia de órdenes.
 * Proporciona métodos de consulta personalizados además de los heredados de
 * JpaRepository.
 *
 * @author StockIA Team
 * @version 1.0
 * @since 2025-11-24
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {

    /**
     * Busca una orden por su número de orden.
     * 
     * @param orderNumber Número de orden a buscar
     * @return Optional con la orden si existe
     */
    Optional<Order> findByOrderNumber(String orderNumber);

    /**
     * Verifica si existe una orden con el número de orden dado.
     * 
     * @param orderNumber Número de orden a verificar
     * @return true si existe, false en caso contrario
     */
    boolean existsByOrderNumber(String orderNumber);

    /**
     * Busca todas las órdenes con un estado específico.
     * Ordena por fecha de orden descendente (más recientes primero).
     * 
     * @param status Estado de las órdenes a buscar
     * @return Lista de órdenes con el estado especificado
     */
    List<Order> findByStatusOrderByOrderDateDesc(OrderStatus status);

    /**
     * Busca todas las órdenes de un cliente específico.
     * Ordena por fecha de orden descendente.
     * 
     * @param customerId ID del cliente
     * @return Lista de órdenes del cliente
     */
    List<Order> findByCustomerIdOrderByOrderDateDesc(UUID customerId);

    /**
     * Busca todas las órdenes creadas por un usuario específico.
     * Ordena por fecha de orden descendente.
     * 
     * @param userId ID del usuario
     * @return Lista de órdenes del usuario
     */
    List<Order> findByUserIdOrderByOrderDateDesc(UUID userId);

    /**
     * Busca órdenes cuya fecha de orden esté entre dos fechas.
     * Útil para reportes y análisis de ventas por período.
     * 
     * @param start Fecha de inicio (inclusive)
     * @param end   Fecha de fin (inclusive)
     * @return Lista de órdenes en el rango de fechas
     */
    List<Order> findByOrderDateBetweenOrderByOrderDateDesc(LocalDateTime start, LocalDateTime end);

    /**
     * Obtiene todas las órdenes ordenadas por fecha descendente.
     * 
     * @return Lista de todas las órdenes
     */
    List<Order> findAllByOrderByOrderDateDesc();
}
