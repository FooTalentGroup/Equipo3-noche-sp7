package com.stockia.stockia.repositories;

import com.stockia.stockia.models.TokenBlacklist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface TokenBlacklistRepository extends JpaRepository<TokenBlacklist, Long> {
    void deleteByExpirationDateBefore(LocalDateTime dateTime);
    boolean existsByToken(String token);
}
