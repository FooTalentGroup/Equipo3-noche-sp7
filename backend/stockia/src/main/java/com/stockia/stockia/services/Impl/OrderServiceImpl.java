package com.stockia.stockia.services.Impl;

import com.stockia.stockia.dtos.order.CancelOrderRequestDto;
import com.stockia.stockia.dtos.order.OrderItemRequestDto;
import com.stockia.stockia.dtos.order.OrderRequestDto;
import com.stockia.stockia.dtos.order.OrderResponseDto;
import com.stockia.stockia.enums.MovementType;
import com.stockia.stockia.enums.OrderStatus;
import com.stockia.stockia.exceptions.UnauthorizedException;
import com.stockia.stockia.exceptions.client.ClientNotFoundException;
import com.stockia.stockia.exceptions.order.InvalidOrderStatusException;
import com.stockia.stockia.exceptions.order.OrderNotFoundException;
import com.stockia.stockia.exceptions.product.InsufficientStockException;
import com.stockia.stockia.exceptions.product.ProductNotFoundException;
import com.stockia.stockia.mappers.OrderMapper;
import com.stockia.stockia.models.*;
import com.stockia.stockia.repositories.*;
import com.stockia.stockia.services.OrderService;
import com.stockia.stockia.services.OrderPdfService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Implementación del servicio de gestión de órdenes de venta.
 * Contiene toda la lógica de negocio para el módulo de ventas incluyendo:
 * - Validación de stock
 * - Bloqueo de inventario
 * - Creación de movimientos de inventario
 * - Gestión de estados de orden
 * - Cálculo de totales y descuentos
 *
 * @author StockIA Team
 * @version 1.0
 * @since 2025-11-24
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;
    private final ClientRepository clientRepository;
    private final UserRepository userRepository;
    private final InventoryMovementRepository inventoryMovementRepository;
    private final OrderMapper orderMapper;
    private final OrderPdfService orderPdfService;

    /**
     * Crea una nueva orden de venta.
     * Implementa RN-01, RN-03, RN-05 y CA-2.
     * 
     * @param dto Datos de la orden
     * @return DTO con la orden creada
     */
    @Override
    @Transactional
    public OrderResponseDto createOrder(OrderRequestDto dto) {
        log.info("Creating new order for customer: {}", dto.getCustomerId());

        // RN-03: Obtener usuario autenticado
        User user = getAuthenticatedUser();

        // Validar que el cliente existe
        Client customer = clientRepository.findById(dto.getCustomerId())
                .orElseThrow(() -> new ClientNotFoundException(dto.getCustomerId()));

        // RN-01: Validar que hay al menos un producto
        if (dto.getItems() == null || dto.getItems().isEmpty()) {
            throw new IllegalArgumentException("La orden debe contener al menos un producto");
        }

        // Crear la orden
        Order order = Order.builder()
                .orderNumber(generateOrderNumber())
                .customer(customer)
                .user(user)
                .status(OrderStatus.PENDING)
                .paymentMethod(dto.getPaymentMethod())
                .paymentNote(dto.getPaymentNote())
                .discountAmount(dto.getDiscountAmount() != null ? dto.getDiscountAmount() : BigDecimal.ZERO)
                .items(new ArrayList<>())
                .build();

        // Procesar cada item
        for (OrderItemRequestDto itemDto : dto.getItems()) {
            processOrderItem(order, itemDto, user);
        }

        // Calcular totales
        order.calculateTotals();

        // Validar que el descuento no exceda el subtotal
        if (order.getDiscountAmount().compareTo(order.getSubtotal()) > 0) {
            throw new IllegalArgumentException("El descuento no puede exceder el subtotal de la orden");
        }

        // Guardar la orden
        Order savedOrder = orderRepository.save(order);
        log.info("Order created successfully with number: {}", savedOrder.getOrderNumber());

        return orderMapper.toResponseDto(savedOrder);
    }

    /**
     * Procesa un item de la orden: valida stock, crea el item y actualiza
     * inventario.
     * Implementa CA-2 (validación de stock) y RN-05 (transaccionalidad).
     */
    private void processOrderItem(Order order, OrderItemRequestDto itemDto, User user) {
        // Buscar el producto
        Product product = productRepository.findById(itemDto.getProductId())
                .orElseThrow(() -> new ProductNotFoundException(itemDto.getProductId()));

        // CA-2: Validar que el producto está disponible
        if (!product.getIsAvailable()) {
            throw new IllegalArgumentException("El producto '" + product.getName() + "' no está disponible para venta");
        }

        // CA-2: Validar stock suficiente
        if (product.getCurrentStock() < itemDto.getQuantity()) {
            throw new InsufficientStockException(
                    String.format("Stock insuficiente para el producto '%s'. Stock actual: %d, cantidad requerida: %d",
                            product.getName(), product.getCurrentStock(), itemDto.getQuantity()));
        }

        // Validar que el descuento no exceda el precio unitario
        if (itemDto.getUnitPrice().compareTo(product.getPrice()) > 0) {
            log.warn("Unit price {} is higher than product price {} for product: {}",
                    itemDto.getUnitPrice(), product.getPrice(), product.getName());
        }

        // Crear el item de orden
        OrderItem orderItem = OrderItem.builder()
                .product(product)
                .quantity(itemDto.getQuantity())
                .unitPrice(itemDto.getUnitPrice())
                .build();

        // Agregar item a la orden
        order.addItem(orderItem);

        // RN-05: Bloquear stock (decrementar inmediatamente)
        int newStock = product.getCurrentStock() - itemDto.getQuantity();
        product.setCurrentStock(newStock);
        productRepository.save(product);

        // Crear movimiento de inventario
        InventoryMovement movement = InventoryMovement.builder()
                .product(product)
                .movementType(MovementType.OUT)
                .quantity(itemDto.getQuantity())
                .newStock(newStock)
                .reason("Venta - Orden: " + order.getOrderNumber())
                .user(user)
                .build();
        inventoryMovementRepository.save(movement);

        log.info("Stock decremented for product: {} ({} units). New stock: {}",
                product.getName(), itemDto.getQuantity(), newStock);
    }

    /**
     * Genera un número de orden único con formato: ORD-YYYYMMDD-XXXX
     */
    private String generateOrderNumber() {
        String datePrefix = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String baseOrderNumber = "ORD-" + datePrefix + "-";

        // Buscar el último número de orden del día
        int sequence = 1;
        String orderNumber;
        do {
            orderNumber = baseOrderNumber + String.format("%04d", sequence);
            sequence++;
        } while (orderRepository.existsByOrderNumber(orderNumber));

        return orderNumber;
    }

    /**
     * Obtiene el usuario autenticado del contexto de seguridad.
     * Implementa RN-03.
     */
    private User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new UnauthorizedException("Usuario no autenticado");
        }

        String email = authentication.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UnauthorizedException("Usuario no encontrado"));
    }

    @Override
    public OrderResponseDto getOrderById(UUID id) {
        log.info("Fetching order by ID: {}", id);
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(id));
        return orderMapper.toResponseDto(order);
    }

    @Override
    public OrderResponseDto getOrderByOrderNumber(String orderNumber) {
        log.info("Fetching order by order number: {}", orderNumber);
        Order order = orderRepository.findByOrderNumber(orderNumber)
                .orElseThrow(() -> new OrderNotFoundException("No se encontró la orden con número: " + orderNumber));
        return orderMapper.toResponseDto(order);
    }

    @Override
    public List<OrderResponseDto> getAllOrders() {
        log.info("Fetching all orders");
        List<Order> orders = orderRepository.findAllByOrderByOrderDateDesc();
        return orderMapper.toResponseDtoList(orders);
    }

    @Override
    public List<OrderResponseDto> getOrdersByStatus(OrderStatus status) {
        log.info("Fetching orders by status: {}", status);
        List<Order> orders = orderRepository.findByStatusOrderByOrderDateDesc(status);
        return orderMapper.toResponseDtoList(orders);
    }

    /**
     * Confirma una orden.
     * Solo se pueden confirmar órdenes en estado PENDING.
     */
    @Override
    @Transactional
    public OrderResponseDto confirmOrder(UUID id) {
        log.info("Confirming order: {}", id);

        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(id));

        if (!order.canBeConfirmed()) {
            throw new InvalidOrderStatusException(order.getStatus().name(), "confirmar");
        }

        order.markAsConfirmed();
        Order savedOrder = orderRepository.save(order);

        log.info("Order confirmed successfully: {}", order.getOrderNumber());
        return orderMapper.toResponseDto(savedOrder);
    }

    /**
     * Cancela una orden.
     * Implementa RN-04: registra motivo y usuario que ejecutó la cancelación.
     * Restaura el stock y crea movimientos de inventario inversos.
     */
    @Override
    @Transactional
    public OrderResponseDto cancelOrder(UUID id, CancelOrderRequestDto dto) {
        log.info("Cancelling order: {}", id);

        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(id));

        if (!order.canBeCancelled()) {
            throw new InvalidOrderStatusException(order.getStatus().name(), "cancelar");
        }

        // RN-04: Obtener usuario que ejecuta la cancelación
        User cancelledByUser = getAuthenticatedUser();

        // Restaurar stock de cada item
        for (OrderItem item : order.getItems()) {
            Product product = item.getProduct();
            int newStock = product.getCurrentStock() + item.getQuantity();
            product.setCurrentStock(newStock);
            productRepository.save(product);

            // Crear movimiento de inventario inverso
            InventoryMovement movement = InventoryMovement.builder()
                    .product(product)
                    .movementType(MovementType.IN)
                    .quantity(item.getQuantity())
                    .newStock(newStock)
                    .reason("Anulación de venta - Orden: " + order.getOrderNumber())
                    .user(cancelledByUser)
                    .build();
            inventoryMovementRepository.save(movement);

            log.info("Stock restored for product: {} ({} units). New stock: {}",
                    product.getName(), item.getQuantity(), newStock);
        }

        // RN-04: Marcar orden como cancelada con motivo y usuario
        order.markAsCancelled(dto.getCancelReason(), cancelledByUser);
        Order savedOrder = orderRepository.save(order);

        log.info("Order cancelled successfully: {}", order.getOrderNumber());
        return orderMapper.toResponseDto(savedOrder);
    }

    /**
     * Marca una orden como entregada.
     * Solo se pueden marcar como entregadas órdenes en estado CONFIRMED.
     */
    @Override
    @Transactional
    public OrderResponseDto markAsDelivered(UUID id) {
        log.info("Marking order as delivered: {}", id);

        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(id));

        if (!order.canBeDelivered()) {
            throw new InvalidOrderStatusException(order.getStatus().name(), "marcar como entregada");
        }

        order.markAsDelivered();
        Order savedOrder = orderRepository.save(order);

        log.info("Order marked as delivered successfully: {}", order.getOrderNumber());
        return orderMapper.toResponseDto(savedOrder);
    }

    /**
     * Genera el PDF del comprobante de venta.
     * Delega a OrderPdfService para la generación del documento.
     */
    @Override
    public byte[] generateOrderPdf(UUID id) {
        log.info("Generating PDF for order: {}", id);

        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(id));

        return orderPdfService.generatePdf(order);
    }
}
