package com.stockia.stockia.repositories;

import com.stockia.stockia.models.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio JPA para la entidad ProductCategory.
 * Proporciona métodos para acceder y manipular categorías de productos en la base de datos.
 *
 * @author StockIA Team
 * @version 1.0
 * @since 2025-11-20
 */
@Repository
public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Long> {

    /**
     * Busca todas las categorías activas.
     * Útil para listar solo las categorías disponibles para asignación a productos.
     *
     * @return lista de categorías con is_active = true
     */
    List<ProductCategory> findByIsActiveTrue();

    /**
     * Busca una categoría por su nombre exacto.
     * Útil para validar duplicados al crear o actualizar categorías.
     *
     * @param name nombre de la categoría a buscar
     * @return Optional con la categoría si existe, Optional.empty() si no
     */
    Optional<ProductCategory> findByName(String name);

    /**
     * Verifica si existe una categoría con el nombre especificado.
     * Optimizado para validaciones de existencia sin cargar la entidad completa.
     *
     * @param name nombre a verificar
     * @return true si existe una categoría con ese nombre, false si no
     */
    boolean existsByName(String name);

    /**
     * Busca una categoría por nombre ignorando mayúsculas/minúsculas.
     * Útil para búsquedas más flexibles.
     *
     * @param name nombre a buscar (case-insensitive)
     * @return Optional con la categoría si existe
     */
    Optional<ProductCategory> findByNameIgnoreCase(String name);

    /**
     * Busca todas las categorías eliminadas (soft delete).
     *
     * @return lista de categorías con deleted = true
     */
    List<ProductCategory> findByDeletedTrue();

    /**
     * Busca todas las categorías no eliminadas.
     *
     * @return lista de categorías con deleted = false
     */
    List<ProductCategory> findByDeletedFalse();
}

