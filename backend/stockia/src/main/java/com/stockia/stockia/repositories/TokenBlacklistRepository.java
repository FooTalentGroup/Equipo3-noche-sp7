package com.stockia.stockia.repositories;

import com.stockia.stockia.models.TokenBlacklist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.UUID;

public interface TokenBlacklistRepository extends JpaRepository<TokenBlacklist, UUID> {
    void deleteByExpirationDateBefore(LocalDateTime dateTime);

    boolean existsByToken(String token);
}
