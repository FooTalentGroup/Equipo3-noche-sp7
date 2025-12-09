package com.stockia.stockia.controllers;


import com.stockia.stockia.documentation.GetHomeStatsEndpointDoc;
import com.stockia.stockia.dtos.HomeStatsResponseDto;
import com.stockia.stockia.services.HomeService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.stockia.stockia.security.constants.SecurityConstants.Roles.ADMIN_OR_MANAGER;

@RestController
@RequestMapping("/api/home")
@RequiredArgsConstructor
@Tag(name = "11 - Home",
        description = "Endpoints para generar estadísticas del home")
public class HomeController {

    private final HomeService homeService;

    @GetMapping
    @GetHomeStatsEndpointDoc
    @PreAuthorize(ADMIN_OR_MANAGER)
    public ResponseEntity<HomeStatsResponseDto> getHomeStats() {
        return ResponseEntity.ok(homeService.getStats());
    }
}
