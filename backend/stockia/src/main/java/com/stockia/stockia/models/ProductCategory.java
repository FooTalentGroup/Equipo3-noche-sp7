package com.stockia.stockia.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Entidad que representa una categoría de productos.
 * Agrupa productos relacionados para facilitar su organización y búsqueda.
 *
 * @author StockIA Team
 * @version 1.0
 * @since 2025-11-20
 */
@Entity
@Table(name = "product_categories", indexes = {
    @Index(name = "idx_category_name", columnList = "name"),
    @Index(name = "idx_category_active", columnList = "is_active")
})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"products"})
@EqualsAndHashCode(of = {"id"})
public class ProductCategory {

    /**
     * Identificador único de la categoría.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Nombre de la categoría.
     * Debe ser único y no puede estar vacío.
     */
    @NotBlank(message = "El nombre de la categoría es obligatorio")
    @Size(max = 50, message = "El nombre no puede exceder 50 caracteres")
    @Column(nullable = false, length = 50, unique = true)
    private String name;

    /**
     * Descripción de la categoría.
     * Campo opcional que proporciona más información sobre la categoría.
     */
    @Size(max = 255, message = "La descripción no puede exceder 255 caracteres")
    @Column(length = 255)
    private String description;

    /**
     * Indica si la categoría está activa.
     * Las categorías inactivas no se muestran en las operaciones normales.
     */
    @Column(name = "is_active", nullable = false)
    @Builder.Default
    private Boolean isActive = true;

    /**
     * Indica si la categoría ha sido eliminada (soft delete).
     * Las categorías eliminadas no se muestran en consultas normales.
     */
    @Column(nullable = false)
    @Builder.Default
    private Boolean deleted = false;

    /**
     * Fecha y hora de eliminación de la categoría (soft delete).
     * Solo se establece cuando la categoría es eliminada.
     */
    @Column(name = "deleted_at")
    private java.time.LocalDateTime deletedAt;

    /**
     * Lista de productos asociados a esta categoría.
     * Relación One-to-Many con carga lazy para optimizar rendimiento.
     * No usar CascadeType.ALL para evitar eliminación accidental de productos.
     */
    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
    @Builder.Default
    private List<Product> products = new ArrayList<>();

    /**
     * Método de utilidad para agregar un producto a la categoría.
     * Mantiene la sincronización bidireccional de la relación.
     *
     * @param product producto a agregar
     */
    public void addProduct(Product product) {
        if (product != null) {
            this.products.add(product);
            product.setCategory(this);
        }
    }

    /**
     * Método de utilidad para remover un producto de la categoría.
     * Mantiene la sincronización bidireccional de la relación.
     *
     * @param product producto a remover
     */
    public void removeProduct(Product product) {
        if (product != null) {
            this.products.remove(product);
            product.setCategory(null);
        }
    }

    /**
     * Método de utilidad para desactivar la categoría.
     * Las categorías inactivas no se muestran en consultas normales.
     */
    public void deactivate() {
        this.isActive = false;
    }

    /**
     * Método de utilidad para activar la categoría.
     */
    public void activate() {
        this.isActive = true;
    }

    /**
     * Método de utilidad para marcar la categoría como eliminada (soft delete).
     */
    public void markAsDeleted() {
        this.deleted = true;
        this.deletedAt = java.time.LocalDateTime.now();
        this.isActive = false;
    }

    /**
     * Método de utilidad para restaurar una categoría eliminada.
     */
    public void restore() {
        this.deleted = false;
        this.deletedAt = null;
    }

    /**
     * Obtiene el número de productos activos en esta categoría.
     *
     * @return cantidad de productos no eliminados
     */
    public long getActiveProductsCount() {
        return products.stream()
            .filter(p -> !p.getDeleted())
            .count();
    }
}

