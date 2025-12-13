package com.stockia.stockia.models;

import java.time.LocalDateTime;
import java.util.UUID;

import com.stockia.stockia.enums.AccountStatus;
import com.stockia.stockia.enums.ClientStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "clients")
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(unique = true, nullable = false)
    private String phone;

    @Builder.Default
    @Column(name = "is_frequent", nullable = false)
    private Boolean isFrequent = false;

    @Enumerated(EnumType.STRING)
    @Column(nullable = true, name = "client_status")
    private ClientStatus clientStatus;

    @PrePersist
    protected void onCreate() {
        clientStatus = ClientStatus.ACTIVE;
        if (email != null) {
            email = email.toLowerCase();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        if (email != null) {
            email = email.toLowerCase();
        }
    }
}
