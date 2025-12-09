package com.stockia.stockia.dtos.auth;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

public record JwtDataDto(
        @Schema(description = "Identificador único del usuario en formato UUID", example = "123e4567-e89b-12d3-a456-426614174000")
        UUID id,
        @Schema(description = "Email del usuario", example = "juan.perez_92@example.com")
        String email,
        @Schema(description = "Nombre del usuario", example = "Juan Perez")
        String name,
        @Schema(description = "Rol o nivel de permisos del usuario en el sistema", example = "MANAGER", allowableValues = {"ADMIN", "MANAGER"})
        String role,
        @Schema(description = "Estado del usuario", example = "ACTIVE")
        String accountStatus
) {
}
