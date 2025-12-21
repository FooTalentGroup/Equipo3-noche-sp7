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

@Repository
public interface ProductCategoryRepository extends JpaRepository<ProductCategory, UUID> {

    List<ProductCategory> findByIsActiveTrueAndDeletedFalse();

    Optional<ProductCategory> findByNameIgnoreCase(String name);

    boolean existsByNameIgnoreCase(String name);

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
