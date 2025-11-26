package com.stockia.stockia.repositories;

import com.stockia.stockia.models.Product;
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
 * Repositorio JPA para la entidad Product.
 * Proporciona métodos para acceder y manipular productos en la base de datos.
 * Incluye query methods para búsquedas, validaciones y filtrado de productos.
 *
 * @author StockIA Team
 * @version 1.0
 * @since 2025-11-20
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {

       /**
        * Busca productos por nombre (búsqueda parcial, case-insensitive) excluyendo
        * eliminados.
        * Útil para funcionalidad de búsqueda y autocompletado.
        *
        * @param name parte del nombre a buscar
        * @return lista de productos cuyo nombre contiene el texto especificado
        */
       List<Product> findByNameContainingIgnoreCaseAndDeletedFalse(String name);

       /**
        * Busca productos por categoría excluyendo eliminados.
        * Útil para listar productos de una categoría específica.
        *
        * @param categoryId ID de la categoría
        * @return lista de productos pertenecientes a la categoría especificada
        */
       List<Product> findByCategoryIdAndDeletedFalse(UUID categoryId);

       /**
        * Busca productos por nombre y categoría (búsqueda combinada) excluyendo
        * eliminados.
        * Útil para filtros avanzados.
        *
        * @param name       parte del nombre a buscar
        * @param categoryId ID de la categoría
        * @return lista de productos que coinciden con ambos criterios
        */
       List<Product> findByNameContainingIgnoreCaseAndCategoryIdAndDeletedFalse(String name, UUID categoryId);

       /**
        * Lista todos los productos activos (no eliminados).
        * Método principal para listar el inventario actual.
        *
        * @return lista de todos los productos con deleted = false
        */
       List<Product> findByDeletedFalse();

       /**
        * Lista todos los productos eliminados (soft delete).
        * Útil para historial y recuperación de productos.
        *
        * @return lista de todos los productos con deleted = true
        */
       List<Product> findByDeletedTrue();

       /**
        * Busca productos con stock actual menor al especificado, excluyendo
        * eliminados.
        * Utilizado para alertas de stock bajo.
        *
        * @param stock umbral de stock a comparar
        * @return lista de productos con currentStock menor al valor especificado
        */
       List<Product> findByCurrentStockLessThanAndDeletedFalse(Integer stock);

       /**
        * Busca productos con stock actual menor o igual al stock mínimo (alertas de
        * stock bajo).
        * Versión alternativa usando consulta JPQL para mayor claridad.
        *
        * @return lista de productos que necesitan reabastecimiento
        */
       @Query("SELECT p FROM Product p WHERE p.currentStock <= p.minStock AND p.deleted = false")
       List<Product> findLowStockProducts();

       /**
        * Busca productos por disponibilidad excluyendo eliminados.
        *
        * @param isAvailable estado de disponibilidad
        * @return lista de productos con la disponibilidad especificada
        */
       List<Product> findByIsAvailableAndDeletedFalse(Boolean isAvailable);

       /**
        * Busca un producto por ID solo si no está eliminado.
        * Alternativa a findById que automáticamente excluye productos eliminados.
        *
        * @param id ID del producto
        * @return Optional con el producto si existe y no está eliminado
        */
       Optional<Product> findByIdAndDeletedFalse(UUID id);

       /**
        * Cuenta productos activos por categoría.
        * Útil para estadísticas y reportes.
        *
        * @param categoryId ID de la categoría
        * @return cantidad de productos activos en la categoría
        */
       long countByCategoryIdAndDeletedFalse(UUID categoryId);

       /**
        * Verifica si existe un producto activo con el mismo nombre.
        * El nombre del producto debe ser único en todo el sistema.
        * Nota: Los nombres se almacenan en lowercase para consistencia.
        *
        * @param name nombre del producto (debe estar en lowercase)
        * @return true si existe un producto activo con ese nombre
        */
       @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END FROM Product p " +
                     "WHERE p.name = :name AND p.deleted = false")
       boolean existsActiveProductByName(@Param("name") String name);

       /**
        * Verifica si existe un producto activo con el mismo nombre, excluyendo un ID
        * específico.
        * Útil para validaciones en actualizaciones.
        * Nota: Los nombres se almacenan en lowercase para consistencia.
        *
        * @param name      nombre del producto (debe estar en lowercase)
        * @param excludeId ID del producto a excluir de la búsqueda
        * @return true si existe otro producto activo con ese nombre
        */
       @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END FROM Product p " +
                     "WHERE p.name = :name AND p.deleted = false AND p.id != :excludeId")
       boolean existsActiveProductByNameExcludingId(@Param("name") String name, @Param("excludeId") UUID excludeId);

       /**
        * Busca productos con filtros dinámicos (nombre, categoría, estado de eliminación,
        * disponibilidad, stock bajo).
        * Soporta paginación y ordenamiento.
        *
        * @param query Filtro por nombre (búsqueda parcial, case-insensitive)
        * @param categoryId Filtro por categoría (null = todas)
        * @param deleted Filtro por estado de eliminación (null = todos)
        * @param includeInactive Incluir productos inactivos (null o false = solo activos)
        * @param lowStock Filtrar solo productos con stock bajo (null o false = todos)
        * @param pageable Configuración de paginación y ordenamiento
        * @return Página de productos que cumplen con los criterios
        */
       @Query("""
               SELECT p FROM Product p
               WHERE
                   (:query IS NULL OR :query = '' OR LOWER(p.name) LIKE LOWER(CONCAT('%', :query, '%'))) AND
                   (:categoryId IS NULL OR p.category.id = :categoryId) AND
                   (:deleted IS NULL OR p.deleted = :deleted) AND
                   (:includeInactive = true OR p.isAvailable = true) AND
                   (:lowStock = false OR p.currentStock <= p.minStock)
               """)
       Page<Product> searchProducts(
               @Param("query") String query,
               @Param("categoryId") UUID categoryId,
               @Param("deleted") Boolean deleted,
               @Param("includeInactive") Boolean includeInactive,
               @Param("lowStock") Boolean lowStock,
               Pageable pageable);
}
