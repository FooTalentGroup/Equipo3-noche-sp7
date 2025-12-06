package com.stockia.stockia.repositories;

import com.stockia.stockia.models.ProductCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repositorio JPA para la entidad ProductCategory.
 * Proporciona métodos para acceder y manipular categorías de productos en la
 * base de datos.
 *
 * @author StockIA Team
 * @version 1.0
 * @since 2025-11-20
 */
@Repository
public interface ProductCategoryRepository extends JpaRepository<ProductCategory, UUID> {

    /**
     * Busca todas las categorías activas y no eliminadas.
     * Útil para listar solo las categorías disponibles para asignación a productos.
     * Sin paginación - ideal para dropdowns y selectores.
     *
     * @return lista de categorías con is_active = true y deleted = false
     */
    List<ProductCategory> findByIsActiveTrueAndDeletedFalse();

    /**
     * Busca una categoría por nombre ignorando mayúsculas/minúsculas.
     * Útil para obtener la entidad completa al validar duplicados.
     *
     * @param name nombre a buscar (case-insensitive)
     * @return Optional con la categoría si existe
     */
    Optional<ProductCategory> findByNameIgnoreCase(String name);

    /**
     * Verifica si existe una categoría con el nombre especificado (case-insensitive).
     * Optimizado para validaciones de existencia sin cargar la entidad completa.
     * Más eficiente que findByNameIgnoreCase().isPresent()
     *
     * @param name nombre a verificar (case-insensitive)
     * @return true si existe una categoría con ese nombre, false si no
     */
    boolean existsByNameIgnoreCase(String name);

    /**
     * Busca categorías con filtros dinámicos (nombre, estado activo, eliminadas).
     * Soporta paginación y ordenamiento.
     *
     * @param name Filtro por nombre (búsqueda parcial, case-insensitive)
     * @param isActive Filtro por estado activo (null = todos)
     * @param deleted Filtro por estado de eliminación (null = todos)
     * @param pageable Configuración de paginación y ordenamiento
     * @return Página de categorías que cumplen con los criterios
     */
    @Query("""
            SELECT c FROM ProductCategory c
            WHERE
                (:name IS NULL OR :name = '' OR LOWER(c.name) LIKE LOWER(CONCAT('%', :name, '%'))) AND
                (:isActive IS NULL OR c.isActive = :isActive) AND
                (:deleted IS NULL OR c.deleted = :deleted)
            """)
    Page<ProductCategory> searchCategories(
            @Param("name") String name,
            @Param("isActive") Boolean isActive,
            @Param("deleted") Boolean deleted,
            Pageable pageable);
}
