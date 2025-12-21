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

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {

        List<Product> findByNameContainingIgnoreCaseAndDeletedFalse(String name);

        List<Product> findByCategoryIdAndDeletedFalse(UUID categoryId);

        List<Product> findByNameContainingIgnoreCaseAndCategoryIdAndDeletedFalse(String name, UUID categoryId);

        List<Product> findByDeletedFalse();

        List<Product> findByDeletedTrue();

        List<Product> findByCurrentStockLessThanAndDeletedFalse(Integer stock);

        @Query("SELECT p FROM Product p WHERE p.currentStock <= p.minStock AND p.deleted = false")
        List<Product> findLowStockProducts();

        @Query("SELECT COUNT(p) FROM Product p WHERE p.currentStock <= p.minStock AND p.deleted = false")
        long countLowStockProducts();

        @Query("""
            SELECT COUNT(p)
            FROM Product p
            WHERE p.deleted = false
              AND p.isAvailable = true
              AND p.currentStock > 0
        """)
        long countActiveProducts();

        @Query("SELECT p.photoUrl FROM Product p WHERE p.id = :id")
        String findPhotoUrlById(@Param("id") UUID id);

        List<Product> findByIsAvailableAndDeletedFalse(Boolean isAvailable);

        Optional<Product> findByIdAndDeletedFalse(UUID id);

        long countByCategoryIdAndDeletedFalse(UUID categoryId);

        @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END FROM Product p " +
                        "WHERE LOWER(p.name) = LOWER(:name) AND p.deleted = false")
        boolean existsActiveProductByName(@Param("name") String name);

        @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END FROM Product p " +
                        "WHERE LOWER(p.name) = LOWER(:name) AND p.deleted = false AND p.id != :excludeId")
        boolean existsActiveProductByNameExcludingId(@Param("name") String name, @Param("excludeId") UUID excludeId);

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
