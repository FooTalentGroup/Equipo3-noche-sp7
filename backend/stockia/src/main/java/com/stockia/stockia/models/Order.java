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

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @NotBlank(message = "El número de orden es obligatorio")
    @Size(max = 20, message = "El número de orden no puede exceder 20 caracteres")
    @Column(name = "order_number", nullable = false, unique = true, length = 20)
    private String orderNumber;

    @NotNull(message = "El cliente es obligatorio")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "customer_id", nullable = false)
    private Client customer;

    @NotNull(message = "El usuario es obligatorio")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @NotNull(message = "El estado es obligatorio")
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    @Builder.Default
    private OrderStatus status = OrderStatus.PENDING;

    @NotNull(message = "El subtotal es obligatorio")
    @DecimalMin(value = "0.0", message = "El subtotal debe ser mayor o igual a 0")
    @Column(nullable = false, precision = 12, scale = 2)
    @Builder.Default
    private BigDecimal subtotal = BigDecimal.ZERO;

    @DecimalMin(value = "0.0", message = "El descuento debe ser mayor o igual a 0")
    @Column(name = "discount_amount", precision = 10, scale = 2)
    @Builder.Default
    private BigDecimal discountAmount = BigDecimal.ZERO;

    @NotNull(message = "El total es obligatorio")
    @DecimalMin(value = "0.0", message = "El total debe ser mayor o igual a 0")
    @Column(name = "total_amount", nullable = false, precision = 12, scale = 2)
    @Builder.Default
    private BigDecimal totalAmount = BigDecimal.ZERO;

    @NotNull(message = "El método de pago es obligatorio")
    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method", nullable = false, length = 20)
    private PaymentMethod paymentMethod;

    @NotNull(message = "El estado del pago es obligatorio")
    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status", nullable = false, length = 20)
    @Builder.Default
    private PaymentStatus paymentStatus = PaymentStatus.PENDING;

    @Size(max = 500, message = "La nota de pago no puede exceder 500 caracteres")
    @Column(name = "payment_note", length = 500)
    private String paymentNote;

    @NotNull(message = "La fecha de orden es obligatoria")
    @Column(name = "order_date", nullable = false)
    private LocalDateTime orderDate;

    @Column(name = "delivered_date")
    private LocalDateTime deliveredDate;

    @Column(name = "cancelled_date")
    private LocalDateTime cancelledDate;

    @Size(max = 500, message = "El motivo de cancelación no puede exceder 500 caracteres")
    @Column(name = "cancel_reason", length = 500)
    private String cancelReason;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cancelled_by")
    private User cancelledBy;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<OrderItem> items = new ArrayList<>();

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        if (orderDate == null) {
            orderDate = LocalDateTime.now();
        }
    }

    public void calculateTotals() {
        this.subtotal = items.stream()
                .map(OrderItem::getItemTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        this.totalAmount = this.subtotal.subtract(
                this.discountAmount != null ? this.discountAmount : BigDecimal.ZERO);
    }

    public void addItem(OrderItem item) {
        items.add(item);
        item.setOrder(this);
    }

    public void removeItem(OrderItem item) {
        items.remove(item);
        item.setOrder(null);
    }

    public void markAsConfirmed() {
        this.status = OrderStatus.CONFIRMED;
        this.paymentStatus = PaymentStatus.PAID;
    }

    public void markAsDelivered() {
        this.status = OrderStatus.DELIVERED;
        this.deliveredDate = LocalDateTime.now();
    }

    public void markAsCancelled(String reason, User cancelledByUser) {
        this.status = OrderStatus.CANCELLED;
        this.cancelledDate = LocalDateTime.now();
        this.cancelReason = reason;
        this.cancelledBy = cancelledByUser;

        if (this.paymentStatus == PaymentStatus.PAID) {
            this.paymentStatus = PaymentStatus.REFUNDED;
        }
    }

    public boolean canBeCancelled() {
        return this.status == OrderStatus.PENDING || this.status == OrderStatus.CONFIRMED;
    }

    public boolean canBeConfirmed() {
        return this.status == OrderStatus.PENDING;
    }

    public boolean canBeDelivered() {
        return this.status == OrderStatus.CONFIRMED;
    }

    public boolean canBeEdited() {
        return this.status == OrderStatus.PENDING;
    }
}
