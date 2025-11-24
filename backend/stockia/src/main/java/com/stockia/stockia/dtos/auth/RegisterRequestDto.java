package com.stockia.stockia.dtos.auth;

import com.stockia.stockia.enums.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

@Schema(description = "DTO para registro de nuevos usuarios")
public record RegisterRequestDto(
        @Schema(description = "Email del usuario", example = "juan.perez@example.com")
        @NotBlank(message = "el email es obligatorio")
        @Size(max = 100, message = "el email no puede superar los 50 caracteres")
        @Email(message = "el email debe tener un formato válido")
        String email,

        @Schema(description = "Contraseña del usuario", example = "Password123@")
        @NotBlank(message = "la contraseña es obligatoria")
        @Size(min = 8, max = 64, message = "La contraseña debe tener entre 8 y 64 caracteres")
        @Pattern(regexp = "^(?=.*[A-ZÑ])(?=.*[a-zñ])(?=.*\\d)(?=.*[-@#$%^&*.,()_+{}|;:'\"<>/!¡¿?])[A-ZÑa-zñ\\d-@#$%^&*.,()_+{}|;:'\"<>/!¡¿?]{6,}$",
                message = "La contraseña debe contener al menos una letra mayuscula, una letra minuscula, un numero, y un caracter especial.")
        String password,

        @Schema(description = "Nombre del usuario", example = "Juan Pérez")
        @NotBlank(message = "el nombre es obligatorio")
        @Size(min = 3, max = 50, message = "el nombre debe tener entre 3 y 50 caracteres")
        String name,

        @Schema(description = "Rol del usuario", example = "MANAGER")
        @NotNull(message = "el rol es obligatorio")
        Role role
) {
}
