package com.stockia.stockia.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.stockia.stockia.models.InventoryMovement;

public interface InventoryMovementRepository extends JpaRepository<InventoryMovement,Long>{

}