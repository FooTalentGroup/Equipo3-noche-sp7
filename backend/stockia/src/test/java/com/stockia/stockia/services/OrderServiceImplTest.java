package com.stockia.stockia.services;

import com.stockia.stockia.dtos.order.CancelOrderRequestDto;
import com.stockia.stockia.dtos.order.EditOrderRequestDto;
import com.stockia.stockia.dtos.order.OrderItemRequestDto;
import com.stockia.stockia.dtos.order.OrderRequestDto;
import com.stockia.stockia.dtos.order.OrderResponseDto;
import com.stockia.stockia.enums.MovementType;
import com.stockia.stockia.enums.OrderStatus;
import com.stockia.stockia.enums.PaymentMethod;
import com.stockia.stockia.enums.PaymentStatus;
import com.stockia.stockia.exceptions.client.ClientNotFoundException;
import com.stockia.stockia.exceptions.UnauthorizedException;
import com.stockia.stockia.exceptions.order.InvalidOrderStatusException;
import com.stockia.stockia.exceptions.order.OrderNotFoundException;
import com.stockia.stockia.exceptions.product.InsufficientStockException;
import com.stockia.stockia.exceptions.product.ProductNotFoundException;
import com.stockia.stockia.mappers.OrderMapper;
import com.stockia.stockia.models.*;
import com.stockia.stockia.repositories.*;
import com.stockia.stockia.services.Impl.OrderServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias para OrderServiceImpl.
 * Valida toda la lógica de negocio del módulo de ventas.
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("OrderService - Unit Tests")
class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderItemRepository orderItemRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private InventoryMovementRepository inventoryMovementRepository;

    @Mock
    private OrderMapper orderMapper;

    @Mock
    private OrderPdfService orderPdfService;

    @InjectMocks
    private OrderServiceImpl orderService;

    private Client testClient;
    private User testUser;
    private Product testProduct;
    private Order testOrder;
    private OrderRequestDto testOrderRequest;

    @BeforeEach
    void setUp() {
        testClient = Client.builder()
                .id(UUID.randomUUID())
                .name("Juan Pérez")
                .email("juan@test.com")
                .phone("1234567890")
                .build();

        testUser = new User();
        testUser.setId(UUID.randomUUID());
        testUser.setEmail("admin@test.com");
        testUser.setName("Admin User");

        testProduct = Product.builder()
                .id(UUID.randomUUID())
                .name("Cheesecake de Fresa")
                .price(BigDecimal.valueOf(45.00))
                .currentStock(10)
                .isAvailable(true)
                .build();

        testOrder = Order.builder()
                .id(UUID.randomUUID())
                .orderNumber("ORD-20251125-0001")
                .customer(testClient)
                .user(testUser)
                .status(OrderStatus.PENDING)
                .paymentMethod(PaymentMethod.CASH)
                .paymentStatus(PaymentStatus.PENDING)
                .items(new ArrayList<>())
                .build();

        OrderItemRequestDto itemRequest = OrderItemRequestDto.builder()
                .productId(testProduct.getId())
                .quantity(2)
                .unitPrice(BigDecimal.valueOf(45.00))
                .build();

        testOrderRequest = OrderRequestDto.builder()
                .customerId(testClient.getId())
                .items(List.of(itemRequest))
                .paymentMethod(PaymentMethod.CASH)
                .discountAmount(BigDecimal.ZERO)
                .build();

        setupSecurityContext();
    }

    private void setupSecurityContext() {
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                "admin@test.com", null, List.of());
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    @DisplayName("Should create order successfully")
    void testCreateOrder_Success() {
        when(userRepository.findByEmail("admin@test.com")).thenReturn(Optional.of(testUser));
        when(clientRepository.findById(testClient.getId())).thenReturn(Optional.of(testClient));
        when(productRepository.findById(testProduct.getId())).thenReturn(Optional.of(testProduct));
        when(orderRepository.save(any(Order.class))).thenAnswer(i -> i.getArguments()[0]);
        when(orderRepository.existsByOrderNumber(anyString())).thenReturn(false);

        OrderResponseDto expectedResponse = new OrderResponseDto();
        when(orderMapper.toResponseDto(any(Order.class))).thenReturn(expectedResponse);

        OrderResponseDto result = orderService.createOrder(testOrderRequest);

        assertNotNull(result);
        verify(productRepository).save(any(Product.class));
        verify(inventoryMovementRepository).save(any(InventoryMovement.class));
        verify(orderRepository).save(any(Order.class));
        assertEquals(8, testProduct.getCurrentStock());
    }

    @Test
    @DisplayName("Should throw exception when client not found")
    void testCreateOrder_ClientNotFound() {
        when(userRepository.findByEmail("admin@test.com")).thenReturn(Optional.of(testUser));
        when(clientRepository.findById(testClient.getId())).thenReturn(Optional.empty());
        assertThrows(ClientNotFoundException.class, () -> orderService.createOrder(testOrderRequest));
        verify(orderRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw exception when product not found")
    void testCreateOrder_ProductNotFound() {
        when(userRepository.findByEmail("admin@test.com")).thenReturn(Optional.of(testUser));
        when(clientRepository.findById(testClient.getId())).thenReturn(Optional.of(testClient));
        when(productRepository.findById(testProduct.getId())).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> orderService.createOrder(testOrderRequest));
        verify(orderRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw exception when insufficient stock")
    void testCreateOrder_InsufficientStock() {
        testProduct.setCurrentStock(1);
        when(userRepository.findByEmail("admin@test.com")).thenReturn(Optional.of(testUser));
        when(clientRepository.findById(testClient.getId())).thenReturn(Optional.of(testClient));
        when(productRepository.findById(testProduct.getId())).thenReturn(Optional.of(testProduct));
        assertThrows(InsufficientStockException.class, () -> orderService.createOrder(testOrderRequest));
        verify(orderRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw exception when order has no items")
    void testCreateOrder_NoItems() {
        OrderRequestDto emptyRequest = OrderRequestDto.builder()
                .customerId(testClient.getId())
                .items(List.of())
                .paymentMethod(PaymentMethod.CASH)
                .build();

        when(userRepository.findByEmail("admin@test.com")).thenReturn(Optional.of(testUser));
        when(clientRepository.findById(testClient.getId())).thenReturn(Optional.of(testClient));

        assertThrows(IllegalArgumentException.class, () -> orderService.createOrder(emptyRequest));
    }

    @Test
    @DisplayName("Should confirm order successfully")
    void testConfirmOrder_Success() {
        when(orderRepository.findById(testOrder.getId())).thenReturn(Optional.of(testOrder));
        when(orderRepository.save(any(Order.class))).thenAnswer(i -> i.getArguments()[0]);

        OrderResponseDto expectedResponse = new OrderResponseDto();
        when(orderMapper.toResponseDto(any(Order.class))).thenReturn(expectedResponse);

        OrderResponseDto result = orderService.confirmOrder(testOrder.getId());

        assertNotNull(result);
        assertEquals(OrderStatus.CONFIRMED, testOrder.getStatus());
        assertEquals(PaymentStatus.PAID, testOrder.getPaymentStatus());
        verify(orderRepository).save(testOrder);
    }

    @Test
    @DisplayName("Should throw exception when confirming non-pending order")
    void testConfirmOrder_InvalidStatus() {
        testOrder.setStatus(OrderStatus.CONFIRMED);
        when(orderRepository.findById(testOrder.getId())).thenReturn(Optional.of(testOrder));
        assertThrows(InvalidOrderStatusException.class,
                () -> orderService.confirmOrder(testOrder.getId()));
    }

    @Test
    @DisplayName("Should cancel order and restore stock")
    void testCancelOrder_Success() {
        OrderItem item = OrderItem.builder()
                .product(testProduct)
                .quantity(2)
                .unitPrice(BigDecimal.valueOf(45.00))
                .build();
        testOrder.addItem(item);

        int initialStock = testProduct.getCurrentStock();

        CancelOrderRequestDto cancelRequest = CancelOrderRequestDto.builder()
                .cancelReason("Cliente solicitó cancelación")
                .build();

        when(orderRepository.findById(testOrder.getId())).thenReturn(Optional.of(testOrder));
        when(userRepository.findByEmail("admin@test.com")).thenReturn(Optional.of(testUser));
        when(orderRepository.save(any(Order.class))).thenAnswer(i -> i.getArguments()[0]);

        OrderResponseDto expectedResponse = new OrderResponseDto();
        when(orderMapper.toResponseDto(any(Order.class))).thenReturn(expectedResponse);

        OrderResponseDto result = orderService.cancelOrder(testOrder.getId(), cancelRequest);

        assertNotNull(result);
        assertEquals(OrderStatus.CANCELLED, testOrder.getStatus());
        assertEquals(initialStock + 2, testProduct.getCurrentStock());
        verify(productRepository).save(testProduct);
        verify(inventoryMovementRepository).save(any(InventoryMovement.class));
        assertEquals("Cliente solicitó cancelación", testOrder.getCancelReason());
        assertNotNull(testOrder.getCancelledDate());
    }

    @Test
    @DisplayName("Should throw exception when cancelling delivered order")
    void testCancelOrder_DeliveredOrder() {
        testOrder.setStatus(OrderStatus.DELIVERED);
        CancelOrderRequestDto cancelRequest = CancelOrderRequestDto.builder()
                .cancelReason("Test")
                .build();

        when(orderRepository.findById(testOrder.getId())).thenReturn(Optional.of(testOrder));

        assertThrows(InvalidOrderStatusException.class,
                () -> orderService.cancelOrder(testOrder.getId(), cancelRequest));
    }

    @Test
    @DisplayName("Should mark order as delivered")
    void testMarkAsDelivered_Success() {
        testOrder.setStatus(OrderStatus.CONFIRMED);
        when(orderRepository.findById(testOrder.getId())).thenReturn(Optional.of(testOrder));
        when(orderRepository.save(any(Order.class))).thenAnswer(i -> i.getArguments()[0]);
        OrderResponseDto expectedResponse = new OrderResponseDto();
        when(orderMapper.toResponseDto(any(Order.class))).thenReturn(expectedResponse);
        OrderResponseDto result = orderService.markAsDelivered(testOrder.getId());
        assertNotNull(result);
        assertEquals(OrderStatus.DELIVERED, testOrder.getStatus());
        assertNotNull(testOrder.getDeliveredDate());
        verify(orderRepository).save(testOrder);
    }

    @Test
    @DisplayName("Should get order by ID")
    void testGetOrderById_Success() {
        when(orderRepository.findById(testOrder.getId())).thenReturn(Optional.of(testOrder));
        OrderResponseDto expectedResponse = new OrderResponseDto();
        when(orderMapper.toResponseDto(testOrder)).thenReturn(expectedResponse);
        OrderResponseDto result = orderService.getOrderById(testOrder.getId());
        assertNotNull(result);
        verify(orderRepository).findById(testOrder.getId());
    }

    @Test
    @DisplayName("Should throw exception when order not found by ID")
    void testGetOrderById_NotFound() {
        UUID nonExistentId = UUID.randomUUID();
        when(orderRepository.findById(nonExistentId)).thenReturn(Optional.empty());
        assertThrows(OrderNotFoundException.class, () -> orderService.getOrderById(nonExistentId));
    }

    @Test
    @DisplayName("Should get all orders")
    void testGetAllOrders() {
        List<Order> orders = List.of(testOrder);
        when(orderRepository.findAllByOrderByOrderDateDesc()).thenReturn(orders);
        when(orderMapper.toResponseDtoList(orders)).thenReturn(new ArrayList<>());
        List<OrderResponseDto> result = orderService.getAllOrders();
        assertNotNull(result);
        verify(orderRepository).findAllByOrderByOrderDateDesc();
    }

    @Test
    @DisplayName("Should filter orders by status")
    void testGetOrdersByStatus() {
        List<Order> orders = List.of(testOrder);
        when(orderRepository.findByStatusOrderByOrderDateDesc(OrderStatus.PENDING))
                .thenReturn(orders);
        when(orderMapper.toResponseDtoList(orders)).thenReturn(new ArrayList<>());

        List<OrderResponseDto> result = orderService.getOrdersByStatus(OrderStatus.PENDING);

        assertNotNull(result);
        verify(orderRepository).findByStatusOrderByOrderDateDesc(OrderStatus.PENDING);
    }

    @Test
    @DisplayName("Should generate PDF for order")
    void testGenerateOrderPdf() {
        byte[] expectedPdf = new byte[] { 1, 2, 3 };
        when(orderRepository.findById(testOrder.getId())).thenReturn(Optional.of(testOrder));
        when(orderPdfService.generatePdf(testOrder)).thenReturn(expectedPdf);

        byte[] result = orderService.generateOrderPdf(testOrder.getId());

        assertArrayEquals(expectedPdf, result);
        verify(orderPdfService).generatePdf(testOrder);
    }

    @Test
    @DisplayName("Should edit order successfully")
    void testEditOrder_Success() {
        OrderItem existingItem = OrderItem.builder()
                .product(testProduct)
                .quantity(2)
                .unitPrice(BigDecimal.valueOf(45.00))
                .build();
        testOrder.addItem(existingItem);

        int initialStock = testProduct.getCurrentStock(); // 10

        Product newProduct = Product.builder()
                .id(UUID.randomUUID())
                .name("Brownie de Chocolate")
                .price(BigDecimal.valueOf(25.00))
                .currentStock(20)
                .isAvailable(true)
                .build();

        OrderItemRequestDto newItemRequest = OrderItemRequestDto.builder()
                .productId(newProduct.getId())
                .quantity(3)
                .unitPrice(BigDecimal.valueOf(25.00))
                .build();

        EditOrderRequestDto editRequest = EditOrderRequestDto.builder()
                .items(List.of(newItemRequest))
                .build();

        when(orderRepository.findById(testOrder.getId())).thenReturn(Optional.of(testOrder));
        when(userRepository.findByEmail("admin@test.com")).thenReturn(Optional.of(testUser));
        when(productRepository.findById(newProduct.getId())).thenReturn(Optional.of(newProduct));
        when(orderRepository.save(any(Order.class))).thenAnswer(i -> i.getArguments()[0]);

        OrderResponseDto expectedResponse = new OrderResponseDto();
        when(orderMapper.toResponseDto(any(Order.class))).thenReturn(expectedResponse);

        OrderResponseDto result = orderService.editOrder(testOrder.getId(), editRequest);

        assertNotNull(result);

        assertEquals(initialStock + 2, testProduct.getCurrentStock());
        assertEquals(17, newProduct.getCurrentStock());

        verify(productRepository, atLeast(2)).save(any(Product.class));

        verify(inventoryMovementRepository, atLeast(2)).save(any(InventoryMovement.class));

        verify(orderRepository).save(any(Order.class));
    }

    @Test
    @DisplayName("Should throw exception when editing order not found")
    void testEditOrder_OrderNotFound() {
        EditOrderRequestDto editRequest = EditOrderRequestDto.builder()
                .items(List.of(OrderItemRequestDto.builder()
                        .productId(testProduct.getId())
                        .quantity(1)
                        .unitPrice(BigDecimal.valueOf(100.00))
                        .build()))
                .build();

        UUID nonExistentId = UUID.randomUUID();
        when(orderRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        assertThrows(OrderNotFoundException.class,
                () -> orderService.editOrder(nonExistentId, editRequest));

        verify(orderRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw exception when editing order not in PENDING status")
    void testEditOrder_InvalidStatus() {
        testOrder.setStatus(OrderStatus.CONFIRMED);

        EditOrderRequestDto editRequest = EditOrderRequestDto.builder()
                .items(List.of(OrderItemRequestDto.builder()
                        .productId(testProduct.getId())
                        .quantity(1)
                        .unitPrice(BigDecimal.valueOf(100.00))
                        .build()))
                .build();

        when(orderRepository.findById(testOrder.getId())).thenReturn(Optional.of(testOrder));

        assertThrows(InvalidOrderStatusException.class,
                () -> orderService.editOrder(testOrder.getId(), editRequest));

        verify(orderRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw exception when user is not order creator")
    void testEditOrder_UnauthorizedUser() {
        User differentUser = new User();
        differentUser.setId(UUID.randomUUID());
        differentUser.setEmail("other@test.com");
        differentUser.setName("Other User");

        EditOrderRequestDto editRequest = EditOrderRequestDto.builder()
                .items(List.of(OrderItemRequestDto.builder()
                        .productId(testProduct.getId())
                        .quantity(1)
                        .unitPrice(BigDecimal.valueOf(100.00))
                        .build()))
                .build();

        when(orderRepository.findById(testOrder.getId())).thenReturn(Optional.of(testOrder));
        when(userRepository.findByEmail("admin@test.com")).thenReturn(Optional.of(differentUser));

        assertThrows(UnauthorizedException.class,
                () -> orderService.editOrder(testOrder.getId(), editRequest));

        verify(orderRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw exception when editing with insufficient stock")
    void testEditOrder_InsufficientStock() {
        OrderItem existingItem = OrderItem.builder()
                .product(testProduct)
                .quantity(2)
                .unitPrice(BigDecimal.valueOf(1000.00))
                .build();
        testOrder.addItem(existingItem);

        // Producto con stock insuficiente
        Product lowStockProduct = Product.builder()
                .id(UUID.randomUUID())
                .name("Tarta de Limón")
                .price(BigDecimal.valueOf(55.00))
                .currentStock(2)
                .isAvailable(true)
                .build();

        OrderItemRequestDto newItemRequest = OrderItemRequestDto.builder()
                .productId(lowStockProduct.getId())
                .quantity(5) // Requiere 5 pero solo hay 2
                .unitPrice(BigDecimal.valueOf(55.00))
                .build();

        EditOrderRequestDto editRequest = EditOrderRequestDto.builder()
                .items(List.of(newItemRequest))
                .build();

        when(orderRepository.findById(testOrder.getId())).thenReturn(Optional.of(testOrder));
        when(userRepository.findByEmail("admin@test.com")).thenReturn(Optional.of(testUser));
        when(productRepository.findById(lowStockProduct.getId())).thenReturn(Optional.of(lowStockProduct));

        assertThrows(InsufficientStockException.class,
                () -> orderService.editOrder(testOrder.getId(), editRequest));

        verify(orderRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw exception when editing with empty items list")
    void testEditOrder_EmptyItems() {
        EditOrderRequestDto editRequest = EditOrderRequestDto.builder()
                .items(List.of())
                .build();

        when(orderRepository.findById(testOrder.getId())).thenReturn(Optional.of(testOrder));
        when(userRepository.findByEmail("admin@test.com")).thenReturn(Optional.of(testUser));

        assertThrows(IllegalArgumentException.class,
                () -> orderService.editOrder(testOrder.getId(), editRequest));

        verify(orderRepository, never()).save(any());
    }
}
