package com.stockia.stockia.models;

import com.stockia.stockia.enums.AccountStatus;
import com.stockia.stockia.enums.Role;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name="users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue
    @Column(updatable = false, nullable = false)
    private UUID id;

    @Column(unique = true)
    private String email;

    @Column(nullable=false,  unique=true)
    private String name;

    @Column(nullable=false)
    private String password; // encriptado (BCrypt)

    @Enumerated(EnumType.STRING)
    @Column(nullable=false)
    private Role role;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AccountStatus account_status;

    @Column(nullable = false)
    private boolean deleted;

    @Column(nullable = false, updatable = false)
    private LocalDateTime created_at;

    private LocalDateTime updated_at;


    @PrePersist
    protected void onCreate() {
        account_status = AccountStatus.ACTIVE;
        deleted = false;
        created_at = LocalDateTime.now();
        updated_at = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updated_at = LocalDateTime.now();
    }
}
