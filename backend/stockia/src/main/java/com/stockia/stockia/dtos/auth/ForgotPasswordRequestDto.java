package com.stockia.stockia.dtos.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Schema(name = "Auth.ForgotPasswordRequestDto",
        description = "Petición para iniciar el proceso de recuperación de contraseña.",
        requiredProperties = {"email"})
public record ForgotPasswordRequestDto(

        @Schema(description = "Correo electronico del usuario.", example = "juan.perez@example.com")
        @Email(message = "email debe tener formato valido")
        @NotBlank(message = "Email requerido")
        String email

) {}

