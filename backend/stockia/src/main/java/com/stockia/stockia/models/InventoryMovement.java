package com.stockia.stockia.models;

import com.stockia.stockia.enums.MovementType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "inventory_movements")
@EntityListeners(AuditingEntityListener.class)
public class InventoryMovement {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @NotNull(message = "El producto es obligatorio")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @NotNull(message = "El tipo de movimiento es obligatorio")
    @Enumerated(EnumType.STRING)
    @Column(name = "movement_type", nullable = false, length = 20)
    private MovementType movementType;

    @NotNull(message = "La cantidad es obligatoria")
    @Column(nullable = false)
    private Integer quantity;

    @Column(length = 255)
    private String reason;

    @NotNull(message = "El usuario es obligatorio")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @NotNull(message = "El stock nuevo es obligatorio")
    @Column(name = "new_stock", nullable = false)
    private Integer newStock;

    @Column(name = "purchase_cost", precision = 10, scale = 2)
    private java.math.BigDecimal purchaseCost;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * Validación personalizada para asegurar que quantity != 0
     */
    @PrePersist
    @PreUpdate
    private void validateQuantity() {
        if (quantity != null && quantity == 0) {
            throw new IllegalArgumentException("La cantidad no puede ser 0");
        }
    }
}
