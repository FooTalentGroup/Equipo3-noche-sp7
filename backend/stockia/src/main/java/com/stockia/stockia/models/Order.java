package com.stockia.stockia.models;

import com.stockia.stockia.enums.OrderStatus;
import com.stockia.stockia.enums.PaymentMethod;
import com.stockia.stockia.enums.PaymentStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Entidad que representa una orden de venta en el sistema.
 * Contiene información completa de la transacción incluyendo cliente,
 * usuario responsable, items, totales, método de pago y estados.
 *
 * @author StockIA Team
 * @version 1.0
 * @since 2025-11-24
 */
@Entity
@Table(name = "orders", indexes = {
        @Index(name = "idx_order_number", columnList = "order_number", unique = true),
        @Index(name = "idx_order_customer", columnList = "customer_id"),
        @Index(name = "idx_order_user", columnList = "user_id"),
        @Index(name = "idx_order_status", columnList = "status"),
        @Index(name = "idx_order_date", columnList = "order_date"),
        @Index(name = "idx_order_payment_status", columnList = "payment_status")
})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = { "items", "customer", "user", "cancelledBy" })
@EqualsAndHashCode(of = { "id" })
@EntityListeners(AuditingEntityListener.class)
public class Order {

    /**
     * Identificador único de la orden.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    /**
     * Número de orden generado automáticamente.
     * Formato: ORD-YYYYMMDD-XXXX (ej: ORD-20251124-0001)
     */
    @NotBlank(message = "El número de orden es obligatorio")
    @Size(max = 20, message = "El número de orden no puede exceder 20 caracteres")
    @Column(name = "order_number", nullable = false, unique = true, length = 20)
    private String orderNumber;

    /**
     * Cliente asociado a la orden.
     * Relación Many-to-One con carga eager.
     */
    @NotNull(message = "El cliente es obligatorio")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "customer_id", nullable = false)
    private Client customer;

    /**
     * Usuario que creó/registró la orden.
     * Relación Many-to-One con carga eager.
     */
    @NotNull(message = "El usuario es obligatorio")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /**
     * Estado actual de la orden.
     */
    @NotNull(message = "El estado es obligatorio")
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    @Builder.Default
    private OrderStatus status = OrderStatus.PENDING;

    /**
     * Subtotal de la orden (suma de items antes de descuentos).
     */
    @NotNull(message = "El subtotal es obligatorio")
    @DecimalMin(value = "0.0", message = "El subtotal debe ser mayor o igual a 0")
    @Column(nullable = false, precision = 12, scale = 2)
    @Builder.Default
    private BigDecimal subtotal = BigDecimal.ZERO;

    /**
     * Monto de descuento aplicado a la orden completa.
     */
    @DecimalMin(value = "0.0", message = "El descuento debe ser mayor o igual a 0")
    @Column(name = "discount_amount", precision = 10, scale = 2)
    @Builder.Default
    private BigDecimal discountAmount = BigDecimal.ZERO;

    /**
     * Total final de la orden (subtotal - descuento).
     */
    @NotNull(message = "El total es obligatorio")
    @DecimalMin(value = "0.0", message = "El total debe ser mayor o igual a 0")
    @Column(name = "total_amount", nullable = false, precision = 12, scale = 2)
    @Builder.Default
    private BigDecimal totalAmount = BigDecimal.ZERO;

    /**
     * Método de pago seleccionado.
     */
    @NotNull(message = "El método de pago es obligatorio")
    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method", nullable = false, length = 20)
    private PaymentMethod paymentMethod;

    /**
     * Estado del pago.
     */
    @NotNull(message = "El estado del pago es obligatorio")
    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status", nullable = false, length = 20)
    @Builder.Default
    private PaymentStatus paymentStatus = PaymentStatus.PENDING;

    /**
     * Nota o comentario sobre el pago (opcional).
     */
    @Size(max = 500, message = "La nota de pago no puede exceder 500 caracteres")
    @Column(name = "payment_note", length = 500)
    private String paymentNote;

    /**
     * Fecha y hora en que se creó la orden.
     * Se establece automáticamente al momento de la creación.
     */
    @NotNull(message = "La fecha de orden es obligatoria")
    @Column(name = "order_date", nullable = false)
    private LocalDateTime orderDate;

    /**
     * Fecha y hora de entrega de la orden.
     * Se establece cuando la orden pasa a estado DELIVERED.
     */
    @Column(name = "delivered_date")
    private LocalDateTime deliveredDate;

    /**
     * Fecha y hora de cancelación de la orden.
     * Se establece cuando la orden pasa a estado CANCELLED.
     */
    @Column(name = "cancelled_date")
    private LocalDateTime cancelledDate;

    /**
     * Motivo de cancelación de la orden.
     */
    @Size(max = 500, message = "El motivo de cancelación no puede exceder 500 caracteres")
    @Column(name = "cancel_reason", length = 500)
    private String cancelReason;

    /**
     * Usuario que ejecutó la cancelación.
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cancelled_by")
    private User cancelledBy;

    /**
     * Items/productos de la orden.
     * Relación One-to-Many con cascade all y orphan removal.
     */
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<OrderItem> items = new ArrayList<>();

    /**
     * Fecha y hora de creación del registro.
     * Se establece automáticamente al crear la orden.
     */
    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * Fecha y hora de la última actualización del registro.
     * Se actualiza automáticamente en cada modificación.
     */
    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /**
     * Establece la fecha de orden al momento de persistir.
     */
    @PrePersist
    protected void onCreate() {
        if (orderDate == null) {
            orderDate = LocalDateTime.now();
        }
    }

    /**
     * Calcula los totales de la orden basándose en los items.
     * Actualiza subtotal y totalAmount.
     */
    public void calculateTotals() {
        this.subtotal = items.stream()
                .map(OrderItem::getItemTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        this.totalAmount = this.subtotal.subtract(
                this.discountAmount != null ? this.discountAmount : BigDecimal.ZERO);
    }

    /**
     * Agrega un item a la orden.
     * Establece la referencia bidireccional.
     * 
     * @param item Item a agregar
     */
    public void addItem(OrderItem item) {
        items.add(item);
        item.setOrder(this);
    }

    /**
     * Remueve un item de la orden.
     * Rompe la referencia bidireccional.
     * 
     * @param item Item a remover
     */
    public void removeItem(OrderItem item) {
        items.remove(item);
        item.setOrder(null);
    }

    /**
     * Marca la orden como confirmada.
     * Cambia el estado a CONFIRMED y actualiza payment_status.
     */
    public void markAsConfirmed() {
        this.status = OrderStatus.CONFIRMED;
        this.paymentStatus = PaymentStatus.PAID;
    }

    /**
     * Marca la orden como entregada.
     * Cambia el estado a DELIVERED y establece la fecha de entrega.
     */
    public void markAsDelivered() {
        this.status = OrderStatus.DELIVERED;
        this.deliveredDate = LocalDateTime.now();
    }

    /**
     * Marca la orden como cancelada.
     * Cambia el estado a CANCELLED, establece fecha, motivo y usuario.
     * 
     * @param reason          Motivo de cancelación
     * @param cancelledByUser Usuario que ejecutó la cancelación
     */
    public void markAsCancelled(String reason, User cancelledByUser) {
        this.status = OrderStatus.CANCELLED;
        this.cancelledDate = LocalDateTime.now();
        this.cancelReason = reason;
        this.cancelledBy = cancelledByUser;

        // Si el pago estaba completado, marcarlo como reembolsado
        if (this.paymentStatus == PaymentStatus.PAID) {
            this.paymentStatus = PaymentStatus.REFUNDED;
        }
    }

    /**
     * Verifica si la orden puede ser cancelada.
     * Solo se pueden cancelar órdenes en estado PENDING o CONFIRMED.
     * 
     * @return true si puede ser cancelada
     */
    public boolean canBeCancelled() {
        return this.status == OrderStatus.PENDING || this.status == OrderStatus.CONFIRMED;
    }

    /**
     * Verifica si la orden puede ser confirmada.
     * Solo se pueden confirmar órdenes en estado PENDING.
     * 
     * @return true si puede ser confirmada
     */
    public boolean canBeConfirmed() {
        return this.status == OrderStatus.PENDING;
    }

    /**
     * Verifica si la orden puede ser marcada como entregada.
     * Solo se pueden marcar como entregadas órdenes en estado CONFIRMED.
     * 
     * @return true si puede ser marcada como entregada
     */
    public boolean canBeDelivered() {
        return this.status == OrderStatus.CONFIRMED;
    }
}
