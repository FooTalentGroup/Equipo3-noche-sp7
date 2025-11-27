package com.stockia.stockia.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Entidad que representa un producto en el sistema de inventario.
 * Incluye información básica del producto, relación con categoría,
 * control de stock y auditoría de cambios.
 *
 * @author StockIA Team
 * @version 1.0
 * @since 2025-11-20
 */
@Entity
@Table(name = "products", indexes = {
        @Index(name = "idx_product_name", columnList = "name"),
        @Index(name = "idx_product_category", columnList = "category_id"),
        @Index(name = "idx_product_deleted", columnList = "deleted"),
        @Index(name = "idx_product_name_category", columnList = "name, category_id")
})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = { "category" })
@EqualsAndHashCode(of = { "id" })
@EntityListeners(AuditingEntityListener.class)
public class Product {

    /**
     * Identificador único del producto.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    /**
     * Nombre del producto.
     * No puede estar vacío, tiene un máximo de 100 caracteres y debe ser único.
     * Los nombres se almacenan en lowercase para consistencia.
     */
    @NotBlank(message = "El nombre del producto es obligatorio")
    @Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
    @Column(nullable = false, length = 100, unique = true)
    private String name;

    /**
     * Categoría a la que pertenece el producto.
     * Relación Many-to-One con carga eager para evitar LazyInitializationException.
     */
    @NotNull(message = "La categoría es obligatoria")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id", nullable = false)
    private ProductCategory category;

    /**
     * Precio de venta del producto.
     * Debe ser mayor o igual a 0.
     */
    @NotNull(message = "El precio es obligatorio")
    @DecimalMin(value = "0.0", message = "El precio debe ser mayor o igual a 0")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    /**
     * URL de la foto del producto.
     * Campo opcional que puede contener la URL donde está almacenada la imagen.
     */
    @Pattern(regexp = "^(https?://.*)?$", message = "La URL de la foto debe ser válida")
    @Column(name = "photo_url")
    private String photoUrl;

    /**
     * Stock actual disponible del producto.
     * No puede ser negativo.
     */
    @NotNull(message = "El stock actual es obligatorio")
    @Min(value = 0, message = "El stock actual debe ser mayor o igual a 0")
    @Column(name = "current_stock", nullable = false)
    @Builder.Default
    private Integer currentStock = 0;

    /**
     * Stock mínimo para generar alertas.
     * Campo opcional con valor por defecto de 5 unidades.
     */
    @Min(value = 0, message = "El stock mínimo debe ser mayor o igual a 0")
    @Column(name = "min_stock", nullable = false)
    @Builder.Default
    private Integer minStock = 5;

    /**
     * Indica si el producto está disponible para venta.
     */
    @Column(name = "is_available", nullable = false)
    @Builder.Default
    private Boolean isAvailable = true;

    /**
     * Fecha y hora de creación del registro.
     * Se establece automáticamente al crear el producto.
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
     * Indica si el producto ha sido eliminado (soft delete).
     * Los productos eliminados no se muestran en consultas normales.
     */
    @Column(nullable = false)
    @Builder.Default
    private Boolean deleted = false;

    /**
     * Fecha y hora de eliminación del producto (soft delete).
     * Solo se establece cuando el producto es eliminado.
     */
    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    /**
     * Método de utilidad para verificar si el producto tiene stock bajo.
     * 
     * @return true si el stock actual es menor que el stock mínimo
     */
    public boolean hasLowStock() {
        return this.currentStock != null && this.minStock != null
                && this.currentStock < this.minStock;
    }

    /**
     * Método de utilidad para marcar el producto como eliminado (soft delete).
     */
    public void markAsDeleted() {
        this.deleted = true;
        this.deletedAt = LocalDateTime.now();
        this.isAvailable = false;
    }

    /**
     * Método de utilidad para restaurar un producto eliminado.
     */
    public void restore() {
        this.deleted = false;
        this.deletedAt = null;
    }
}
