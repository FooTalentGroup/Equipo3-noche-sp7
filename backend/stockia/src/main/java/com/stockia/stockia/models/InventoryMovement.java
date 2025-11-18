package com.stockia.stockia.models;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.time.LocalTime;


@Entity
public class InventoryMovement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    private String movementType;
    private int quantity;
    private String reason;

    private LocalDateTime createdAt= LocalDateTime.now();

    public InventoryMovement(){}
}
