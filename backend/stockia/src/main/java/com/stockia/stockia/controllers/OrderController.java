package com.stockia.stockia.controllers;

import com.stockia.stockia.documentation.order.*;
import com.stockia.stockia.dtos.order.CancelOrderRequestDto;
import com.stockia.stockia.dtos.order.OrderRequestDto;
import com.stockia.stockia.dtos.order.OrderResponseDto;
import com.stockia.stockia.dtos.order.OrderSearchRequestDto;
import com.stockia.stockia.enums.OrderStatus;
import com.stockia.stockia.services.OrderService;
import com.stockia.stockia.utils.ApiResult;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static com.stockia.stockia.security.constants.SecurityConstants.Roles.*;

/**
 * Controlador REST para la gestión de órdenes de venta.
 *
 * Endpoints disponibles:
 * 
 * - POST [/api/orders] → Crear nueva orden
 * - GET [/api/orders/{id}] → Obtener orden por ID
 * - GET [/api/orders] → Listar todas las órdenes
 * - GET [/api/orders/status/{status}] → Filtrar órdenes por estado
 * - PATCH [/api/orders/{id}/confirm] → Confirmar orden
 * - PATCH [/api/orders/{id}/cancel] → Cancelar orden
 * - PATCH [/api/orders/{id}/deliver] → Marcar como entregada
 * - GET [/api/orders/{id}/pdf] → Generar comprobante PDF
 * 
 * @author StockIA Team
 * @version 1.0
 * @since 2025-11-24
 */
@RestController
@RequestMapping("/api/orders")
@Tag(name = "06 - Órdenes de Venta", description = "Endpoints para la gestión de ventas y órdenes")
@RequiredArgsConstructor
@Slf4j
public class OrderController {

    private final OrderService orderService;

    /**
     * Crea una nueva orden de venta.
     * 
     * @param dto Datos de la orden a crear
     * @return Orden creada con estado PENDING
     */
    @PostMapping
    @CreateOrderDoc
    @PreAuthorize(ADMIN_OR_MANAGER)
    public ResponseEntity<ApiResult<OrderResponseDto>> createOrder(@Valid @RequestBody OrderRequestDto dto) {
        log.info("POST /api/orders - Creating new order for customer: {}", dto.getCustomerId());
        OrderResponseDto order = orderService.createOrder(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .header("Location", "/api/orders/" + order.getId())
                .body(ApiResult.success("Orden creada exitosamente", order));
    }

    /**
     * Obtiene una orden por su ID.
     * 
     * @param id ID de la orden
     * @return Orden encontrada
     */
    @GetMapping("/{id}")
    @GetOrderByIdDoc
    @PreAuthorize(ADMIN_OR_MANAGER)
    public ResponseEntity<ApiResult<OrderResponseDto>> getOrderById(@PathVariable UUID id) {
        log.info("GET /api/orders/{} - Fetching order", id);
        OrderResponseDto order = orderService.getOrderById(id);
        return ResponseEntity.ok(ApiResult.success("Orden encontrada", order));
    }

    /**
     * Obtiene todas las órdenes del sistema con paginación y filtros opcionales.
     *
     * Permite filtrar por:
     * - orderNumber: Número de orden (búsqueda parcial)
     * - customerName: Nombre del cliente (búsqueda parcial)
     * - status: Estado de la orden (PENDING, CONFIRMED, DELIVERED, CANCELLED)
     * - paymentMethod: Método de pago
     * - paymentStatus: Estado del pago
     * - startDate y endDate: Rango de fechas
     *
     * Ejemplos de uso:
     * - GET /api/orders?page=0&size=20
     * - GET /api/orders?status=CONFIRMED&page=0&size=10
     * - GET /api/orders?customerName=María&page=0
     * - GET /api/orders?startDate=2025-11-01&endDate=2025-11-30
     *
     * @param searchParams Parámetros de búsqueda opcionales
     * @param pageable     Configuración de paginación y ordenamiento
     * @return Página de órdenes que cumplen con los criterios
     */
    @GetMapping
    @GetAllOrdersDoc
    @PreAuthorize(ADMIN_OR_MANAGER)
    public ResponseEntity<ApiResult<Page<OrderResponseDto>>> getAllOrders(
            @org.springdoc.core.annotations.ParameterObject OrderSearchRequestDto searchParams,
            @org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.info("GET /api/orders - Fetching orders with filters");
        Page<OrderResponseDto> ordersPage = orderService.searchOrders(searchParams, pageable);
        String message = ordersPage.isEmpty()
                ? "No se encontraron órdenes"
                : String.format("%d orden(es) encontrada(s)", ordersPage.getTotalElements());
        return ResponseEntity.ok(ApiResult.success(message, ordersPage));
    }

    /**
     * Obtiene órdenes filtradas por estado.
     * 
     * @param status Estado de las órdenes
     * @return Lista de órdenes con el estado especificado
     */
    @GetMapping("/status/{status}")
    @GetOrdersByStatusDoc
    @PreAuthorize(ADMIN_OR_MANAGER)
    public ResponseEntity<ApiResult<List<OrderResponseDto>>> getOrdersByStatus(@PathVariable OrderStatus status) {
        log.info("GET /api/orders/status/{} - Fetching orders by status", status);
        List<OrderResponseDto> orders = orderService.getOrdersByStatus(status);
        String message = orders.isEmpty() ? "No hay órdenes con estado " + status
                : orders.size() + " orden(es) encontrada(s)";
        return ResponseEntity.ok(ApiResult.success(message, orders));
    }

    /**
     * Confirma una orden en estado PENDING.
     * 
     * @param id ID de la orden a confirmar
     * @return Orden confirmada
     */
    @PatchMapping("/{id}/confirm")
    @ConfirmOrderDoc
    @PreAuthorize(ADMIN_OR_MANAGER)
    public ResponseEntity<ApiResult<OrderResponseDto>> confirmOrder(@PathVariable UUID id) {
        log.info("PATCH /api/orders/{}/confirm - Confirming order", id);
        OrderResponseDto order = orderService.confirmOrder(id);
        return ResponseEntity.ok(ApiResult.success("Orden confirmada exitosamente", order));
    }

    /**
     * Cancela una orden y restaura el stock.
     * 
     * @param id  ID de la orden a cancelar
     * @param dto Datos de cancelación (motivo)
     * @return Orden cancelada
     */
    @PatchMapping("/{id}/cancel")
    @CancelOrderDoc
    @PreAuthorize(ADMIN_OR_MANAGER)
    public ResponseEntity<ApiResult<OrderResponseDto>> cancelOrder(
            @PathVariable UUID id,
            @Valid @RequestBody CancelOrderRequestDto dto) {
        log.info("PATCH /api/orders/{}/cancel - Cancelling order", id);
        OrderResponseDto order = orderService.cancelOrder(id, dto);
        return ResponseEntity.ok(ApiResult.success("Orden cancelada exitosamente", order));
    }

    /**
     * Marca una orden como entregada.
     * 
     * @param id ID de la orden a marcar como entregada
     * @return Orden marcada como entregada
     */
    @PatchMapping("/{id}/deliver")
    @DeliverOrderDoc
    @PreAuthorize(ADMIN_OR_MANAGER)
    public ResponseEntity<ApiResult<OrderResponseDto>> markAsDelivered(@PathVariable UUID id) {
        log.info("PATCH /api/orders/{}/deliver - Marking order as delivered", id);
        OrderResponseDto order = orderService.markAsDelivered(id);
        return ResponseEntity.ok(ApiResult.success("Orden marcada como entregada exitosamente", order));
    }

    /**
     * Genera y descarga el comprobante PDF de una orden.
     * 
     * @param id ID de la orden
     * @return Archivo PDF del comprobante
     */
    @GetMapping("/{id}/pdf")
    @GenerateOrderPdfDoc
    @PreAuthorize(ADMIN_OR_MANAGER)
    public ResponseEntity<byte[]> generateOrderPdf(@PathVariable UUID id) {
        log.info("GET /api/orders/{}/pdf - Generating PDF", id);
        byte[] pdfBytes = orderService.generateOrderPdf(id);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "orden_" + id + ".pdf");
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");

        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
    }
}
