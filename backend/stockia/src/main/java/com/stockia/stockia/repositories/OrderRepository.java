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

/**
 * Repositorio para gestionar las operaciones de persistencia de órdenes.
 * Proporciona métodos de consulta personalizados además de los heredados de
 * JpaRepository.
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
        List<Order> findByCustomer_IdOrderByOrderDateDesc(UUID customerId);

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
         * 
         */

        /**
         * Busca todas las órdenes de un cliente específico ordenadas por fecha
         * descendente.
         * 
         * Este método de consulta recupera el historial completo de compras de un
         * cliente,
         * ordenado cronológicamente de la orden más reciente a la más antigua. Es
         * utilizado
         * principalmente para generar historiales de compra y análisis de
         * comportamiento.
         * 
         * Características de la consulta:
         * - Busca por customer.id (relación JPA)
         * - Ordena por orderDate DESC (más recientes primero)
         * - Retorna todas las órdenes sin filtros de estado
         * - Utiliza índices de base de datos para optimización
         * 
         * Casos de uso:
         * - Generación de historial de compras para clientes
         * - Análisis de frecuencia de compra
         * - Reportes de ventas por cliente
         * - Soporte al cliente y consultas de órdenes
         * 
         * @param customerId ID del cliente (UUID) - debe existir en la tabla clients
         * @return Lista ordenada de órdenes del cliente:
         *         - Lista con órdenes si el cliente tiene compras
         *         - Lista vacía si el cliente no tiene órdenes
         * 
         * @implNote Genera SQL: SELECT * FROM orders WHERE customer_id = ? ORDER BY
         *           order_date DESC
         * @implNote Utiliza índice compuesto en (customer_id, order_date) para
         *           optimización
         * @implNote No valida existencia del cliente - esa responsabilidad es del
         *           servicio
         * 
         * @see Order#getCustomer()
         * @see Order#getOrderDate()
         * 
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

        /**
         * Calcula las métricas de ventas para un período específico.
         * 
         * Retorna:
         * - Total de ingresos (suma de totalAmount)
         * - Cantidad de órdenes
         * - Ticket promedio (promedio de totalAmount)
         * 
         * Solo considera órdenes CONFIRMED o DELIVERED.
         * Permite filtrar por nombre de producto (opcional).
         * 
         * @param startDate   Fecha de inicio del período
         * @param endDate     Fecha de fin del período
         * @param productName Nombre del producto (opcional)
         * @return DTO con las métricas calculadas
         */
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

        /**
         * Obtiene las ventas diarias agrupadas por fecha.
         * 
         * Retorna la suma de totalAmount por cada día del período.
         * Solo considera órdenes CONFIRMED o DELIVERED.
         * Permite filtrar por nombre de producto (opcional).
         * 
         * @param startDate   Fecha de inicio del período
         * @param endDate     Fecha de fin del período
         * @param productName Nombre del producto (opcional)
         * @return Lista de ventas diarias ordenadas por fecha
         */
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

        /**
         * Obtiene la distribución de ventas por método de pago.
         * 
         * Retorna la suma de totalAmount agrupada por paymentMethod.
         * Solo considera órdenes CONFIRMED o DELIVERED.
         * Permite filtrar por nombre de producto (opcional).
         * 
         * @param startDate   Fecha de inicio del período
         * @param endDate     Fecha de fin del período
         * @param productName Nombre del producto (opcional)
         * @return Lista de distribución por método de pago
         */
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
}