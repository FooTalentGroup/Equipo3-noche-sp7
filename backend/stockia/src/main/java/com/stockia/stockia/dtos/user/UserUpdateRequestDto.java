package com.stockia.stockia.dtos.user;

import com.stockia.stockia.enums.AccountStatus;
import com.stockia.stockia.enums.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

public record UserUpdateRequestDto (
    @Schema(description = "Email del usuario.", example = "juan.perez@example.com")
    @Size(max = 100, message = "el email no puede superar los 100 caracteres")
    @Email(message = "el email debe tener un formato válido")
    String email,

    @Schema(description = "Nombre del usuario.", example = "Juan Pérez")
    @Size(min = 3, max = 50, message = "el nombre debe tener entre 3 y 50 caracteres")
    String name,

    @Schema(description = "Rol del usuario. Valores permitidos: ADMIN, MANAGER, USER",
            example = "ADMIN",
            allowableValues = {"ADMIN", "MANAGER", "USER"})
    Role role,

     @Schema(description = "Estado del usuario.",
             example = "ACTIVE",
             allowableValues = {"ACTIVE", "INACTIVE", "SUSPENDED", "ON_VACATION", "BLOCKED"})
    AccountStatus accountStatus
){}
