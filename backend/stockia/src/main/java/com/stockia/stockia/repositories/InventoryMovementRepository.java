package com.stockia.stockia.repositories;

import com.stockia.stockia.enums.MovementType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.stockia.stockia.models.InventoryMovement;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface InventoryMovementRepository extends JpaRepository<InventoryMovement, UUID>{
    @Query("""
                            SELECT i FROM InventoryMovement i
                            WHERE
                                (:productId IS NULL OR i.product.id = :productId) AND
                                (:movementType IS NULL OR i.movementType = :movementType) AND
                                (:userId IS NULL OR i.user.id = :userId)
                        """)
    Page<InventoryMovement> searchInventoryMovements(
            @Param("productId") UUID productId,
            @Param("movementType") MovementType movementType,
            @Param("userId") UUID userId,
            Pageable pageable);
}