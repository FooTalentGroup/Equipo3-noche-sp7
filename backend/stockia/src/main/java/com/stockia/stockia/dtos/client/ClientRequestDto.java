package com.stockia.stockia.dtos.client;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

@Schema(description = "DTO para registrar o actualizar un cliente")
public class ClientRequestDto {

    @Schema(description = "Nombre completo del cliente", example = "María García López", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "El nombre es obligatorio")
    private String name;

    @Schema(description = "Correo electrónico del cliente (debe ser único)", example = "maria.garcia@example.com", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "El correo electrónico es obligatorio")
    @Email(message = "El formato del correo electrónico es inválido")
    private String email;

    @Schema(description = "Número telefónico con código de país (debe ser único)", example = "+5491123456789", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "El teléfono es obligatorio")
    @Pattern(regexp = "^\\+[1-9][0-9]{1,3}[0-9]{8,12}$", message = "El teléfono debe comenzar con código de país (+XX) seguido de 8-12 dígitos. Ejemplo: +54911234567")
    private String phone;

    @Schema(description = "Indica si el cliente es frecuente (true) o no (false)", example = "true", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Debe indicar si es cliente frecuente")
    private Boolean isFrequent;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Boolean getIsFrequent() {
        return isFrequent;
    }

    public void setIsFrequent(Boolean isFrequent) {
        this.isFrequent = isFrequent;
    }
}