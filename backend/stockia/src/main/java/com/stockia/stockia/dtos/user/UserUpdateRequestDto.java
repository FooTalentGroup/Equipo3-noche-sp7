package com.stockia.stockia.dtos.user;

import com.stockia.stockia.enums.AccountStatus;
import com.stockia.stockia.enums.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

public record UserUpdateRequestDto (
    @Schema(description = "Email del usuario.", example = "juan.perez@example.com")
    @Size(max = 100, message = "el email no puede superar los 100 caracteres")
    @Email(message = "el email debe tener un formato válido")
    @Pattern(
            regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$",
            message = "el email debe tener un dominio válido (ej: usuario@dominio.com)"
    )
    String email,

    @Schema(description = "Nombre del usuario.", example = "Juan Pérez")
    @Size(min = 3, max = 50, message = "el nombre debe tener entre 3 y 50 caracteres")
    String name,

    @Schema(description = "Rol del usuario. Valores permitidos: ADMIN, MANAGER",
            example = "ADMIN",
            allowableValues = {"ADMIN", "MANAGER"})
    Role role,

     @Schema(description = "Estado del usuario.",
             example = "ACTIVE",
             allowableValues = {"ACTIVE", "INACTIVE", "SUSPENDED", "ON_VACATION", "BLOCKED"})
    AccountStatus accountStatus
){}
