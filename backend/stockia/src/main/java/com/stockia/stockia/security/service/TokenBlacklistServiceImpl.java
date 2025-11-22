package com.stockia.stockia.security.service;

import com.stockia.stockia.models.TokenBlacklist;
import com.stockia.stockia.repositories.TokenBlacklistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TokenBlacklistServiceImpl implements TokenBlacklistService{
    private final TokenBlacklistRepository tokenBlacklistRepository;

    public void addTokenToBlacklist(String token,  LocalDateTime tokenExpirationDate) {
        TokenBlacklist blacklistedToken = new TokenBlacklist(null, token, tokenExpirationDate);
        tokenBlacklistRepository.save(blacklistedToken);
    }

    public boolean isTokenBlacklisted(String token) {
        return tokenBlacklistRepository.existsByToken(token);
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void deleteExpiredTokens() {
        tokenBlacklistRepository.deleteByExpirationDateBefore(LocalDateTime.now());
    }

}
