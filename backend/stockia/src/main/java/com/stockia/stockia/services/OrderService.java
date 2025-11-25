package com.stockia.stockia.services;

import com.stockia.stockia.dtos.order.CancelOrderRequestDto;
import com.stockia.stockia.dtos.order.OrderRequestDto;
import com.stockia.stockia.dtos.order.OrderResponseDto;
import com.stockia.stockia.enums.OrderStatus;

import java.util.List;
import java.util.UUID;

/**
 * Servicio para la gestión de órdenes de venta.
 * Define las operaciones disponibles para el módulo de ventas.
 *
 * @author StockIA Team
 * @version 1.0
 * @since 2025-11-24
 */
public interface OrderService {

    /**
     * Crea una nueva orden de venta.
     * Valida stock, bloquea inventario y crea movimientos de inventario.
     * 
     * @param dto Datos de la orden a crear
     * @return DTO con la información de la orden creada
     */
    OrderResponseDto createOrder(OrderRequestDto dto);

    /**
     * Obtiene una orden por su ID.
     * 
     * @param id ID de la orden
     * @return DTO con la información de la orden
     */
    OrderResponseDto getOrderById(UUID id);

    /**
     * Obtiene una orden por su número de orden.
     * 
     * @param orderNumber Número de orden
     * @return DTO con la información de la orden
     */
    OrderResponseDto getOrderByOrderNumber(String orderNumber);

    /**
     * Obtiene todas las órdenes del sistema.
     * Ordenadas por fecha descendente (más recientes primero).
     * 
     * @return Lista de órdenes
     */
    List<OrderResponseDto> getAllOrders();

    /**
     * Obtiene todas las órdenes con un estado específico.
     * 
     * @param status Estado de las órdenes a buscar
     * @return Lista de órdenes con el estado especificado
     */
    List<OrderResponseDto> getOrdersByStatus(OrderStatus status);

    /**
     * Confirma una orden en estado PENDING.
     * Cambia el estado a CONFIRMED.
     * 
     * @param id ID de la orden a confirmar
     * @return DTO con la información de la orden confirmada
     */
    OrderResponseDto confirmOrder(UUID id);

    /**
     * Cancela una orden.
     * Restaura el stock y crea movimientos de inventario inversos.
     * Solo se pueden cancelar órdenes en estado PENDING o CONFIRMED.
     * 
     * @param id  ID de la orden a cancelar
     * @param dto Datos de cancelación (motivo)
     * @return DTO con la información de la orden cancelada
     */
    OrderResponseDto cancelOrder(UUID id, CancelOrderRequestDto dto);

    /**
     * Marca una orden como entregada.
     * Solo se pueden marcar como entregadas órdenes en estado CONFIRMED.
     * 
     * @param id ID de la orden a marcar como entregada
     * @return DTO con la información de la orden entregada
     */
    OrderResponseDto markAsDelivered(UUID id);

    /**
     * Genera el PDF del comprobante de venta.
     * 
     * @param id ID de la orden
     * @return Bytes del archivo PDF generado
     */
    byte[] generateOrderPdf(UUID id);
}
