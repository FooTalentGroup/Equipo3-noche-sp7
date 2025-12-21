package com.stockia.stockia.controllers;

import com.stockia.stockia.documentation.order.*;
import com.stockia.stockia.dtos.order.CancelOrderRequestDto;
import com.stockia.stockia.dtos.order.EditOrderRequestDto;
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

@RestController
@RequestMapping("/api/orders")
@Tag(name = "06 - Órdenes de Venta", description = "Endpoints para la gestión de ventas y órdenes")
@RequiredArgsConstructor
@Slf4j
public class OrderController {

    private final OrderService orderService;

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

    @GetMapping("/{id}")
    @GetOrderByIdDoc
    @PreAuthorize(ADMIN_OR_MANAGER)
    public ResponseEntity<ApiResult<OrderResponseDto>> getOrderById(@PathVariable UUID id) {
        log.info("GET /api/orders/{} - Fetching order", id);
        OrderResponseDto order = orderService.getOrderById(id);
        return ResponseEntity.ok(ApiResult.success("Orden encontrada", order));
    }

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

    @PatchMapping("/{id}/confirm")
    @ConfirmOrderDoc
    @PreAuthorize(ADMIN_OR_MANAGER)
    public ResponseEntity<ApiResult<OrderResponseDto>> confirmOrder(@PathVariable UUID id) {
        log.info("PATCH /api/orders/{}/confirm - Confirming order", id);
        OrderResponseDto order = orderService.confirmOrder(id);
        return ResponseEntity.ok(ApiResult.success("Orden confirmada exitosamente", order));
    }

    @PutMapping("/{id}")
    @EditOrderDoc
    @PreAuthorize(ADMIN_OR_MANAGER)
    public ResponseEntity<ApiResult<OrderResponseDto>> editOrder(
            @PathVariable UUID id,
            @Valid @RequestBody EditOrderRequestDto dto) {
        log.info("PUT /api/orders/{} - Editing order", id);
        OrderResponseDto order = orderService.editOrder(id, dto);
        return ResponseEntity.ok(ApiResult.success("Orden editada exitosamente", order));
    }

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

    @PatchMapping("/{id}/deliver")
    @DeliverOrderDoc
    @PreAuthorize(ADMIN_OR_MANAGER)
    public ResponseEntity<ApiResult<OrderResponseDto>> markAsDelivered(@PathVariable UUID id) {
        log.info("PATCH /api/orders/{}/deliver - Marking order as delivered", id);
        OrderResponseDto order = orderService.markAsDelivered(id);
        return ResponseEntity.ok(ApiResult.success("Orden marcada como entregada exitosamente", order));
    }

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
