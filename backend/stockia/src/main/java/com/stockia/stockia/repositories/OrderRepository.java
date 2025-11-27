package com.stockia.stockia.repositories;

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

        /**
         * Busca órdenes con filtros múltiples y paginación.
         *
         * Permite filtrar por número de orden, nombre de cliente, estado, método de
         * pago,
         * estado de pago y rango de fechas. Todos los filtros son opcionales.
         *
         * USA CAST para ayudar a PostgreSQL a determinar tipos de parámetros
         * opcionales.
         *
         * @param orderNumber   Número de orden (búsqueda parcial)
         * @param customerName  Nombre del cliente (búsqueda parcial)
         * @param status        Estado de la orden
         * @param paymentMethod Método de pago
         * @param paymentStatus Estado del pago
         * @param startDate     Fecha de inicio del rango
         * @param endDate       Fecha de fin del rango
         * @param pageable      Configuración de paginación y ordenamiento
         * @return Page con las órdenes que cumplen los criterios
         */
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
}
