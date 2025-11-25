package com.stockia.stockia.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Entidad que representa un item/línea dentro de una orden de venta.
 * Contiene el detalle de cada producto vendido incluyendo cantidad,
 * precio unitario al momento de la venta y total del item.
 *
 * @author StockIA Team
 * @version 1.0
 * @since 2025-11-24
 */
@Entity
@Table(name = "order_items", indexes = {
        @Index(name = "idx_order_item_order", columnList = "order_id"),
        @Index(name = "idx_order_item_product", columnList = "product_id")
})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = { "order", "product" })
@EqualsAndHashCode(of = { "id" })
public class OrderItem {

    /**
     * Identificador único del item.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    /**
     * Orden a la que pertenece este item.
     * Relación Many-to-One.
     */
    @NotNull(message = "La orden es obligatoria")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    /**
     * Producto vendido en este item.
     * Relación Many-to-One con carga eager para obtener detalles del producto.
     */
    @NotNull(message = "El producto es obligatorio")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    /**
     * Cantidad de unidades del producto.
     */
    @NotNull(message = "La cantidad es obligatoria")
    @Min(value = 1, message = "La cantidad debe ser al menos 1")
    @Column(nullable = false)
    private Integer quantity;

    /**
     * Precio unitario del producto al momento de la venta.
     * Se captura en el momento de la venta para mantener histórico
     * incluso si el precio del producto cambia posteriormente.
     */
    @NotNull(message = "El precio unitario es obligatorio")
    @DecimalMin(value = "0.0", message = "El precio unitario debe ser mayor o igual a 0")
    @Column(name = "unit_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal unitPrice;

    /**
     * Total del item (cantidad * precio unitario).
     * Se calcula automáticamente.
     */
    @NotNull(message = "El total del item es obligatorio")
    @DecimalMin(value = "0.0", message = "El total del item debe ser mayor o igual a 0")
    @Column(name = "item_total", nullable = false, precision = 12, scale = 2)
    private BigDecimal itemTotal;

    /**
     * Validación personalizada antes de persistir o actualizar.
     * Asegura que la cantidad sea mayor a 0.
     */
    @PrePersist
    @PreUpdate
    private void validateQuantity() {
        if (quantity != null && quantity <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a 0");
        }
        // Calcular el total del item automáticamente
        calculateItemTotal();
    }

    /**
     * Calcula el total del item basándose en cantidad y precio unitario.
     * Este método se llama automáticamente en @PrePersist y @PreUpdate.
     */
    public void calculateItemTotal() {
        if (quantity != null && unitPrice != null) {
            this.itemTotal = unitPrice.multiply(BigDecimal.valueOf(quantity));
        }
    }
}
